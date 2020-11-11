package model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MyClientHandler implements ClientHandler {

	Solver<Searchable<Position>, String> solver;
	CacheManager<Searchable<Position>, String> cm;
	String endsign = "end";

	public MyClientHandler() {
		solver = new SearcherToSolver(new BestfirstSearch<Position>());
		cm = new FileCacheManager<Searchable<Position>, String>();
	}

	public void handelClient(InputStream inFromClient, OutputStream outToClient) throws IOException {

		BufferedReader inClient = new BufferedReader(new InputStreamReader(inFromClient));
		PrintWriter outClient = new PrintWriter(outToClient);

		double[][] mat;
		String problem;
		List<String> problemList = new ArrayList<>();
		String[] strArr;

		while (!(problem = inClient.readLine()).equals(endsign))
			problemList.add(problem);

		Position start = new Position(inClient.readLine());
		Position target = new Position(inClient.readLine());


		strArr = problemList.get(0).split(",");

		mat = new double[problemList.size()][strArr.length];

		for (int i = 0; i < problemList.size(); i++) {
			strArr = problemList.get(i).split(",");
			
			for (int j = 0; j < strArr.length; j++)
				mat[i][j] = Double.parseDouble(strArr[j]);

		}

		Searchable<Position> s = new commonsearchable(mat, start, target);
		if (cm.isexist(s))
			outClient.println(cm.querySolution(s));
		else
			cm.saveSolution(s, solver.solve(s));

		outClient.println(cm.querySolution(s));
		outClient.flush();
		outClient.close();
		inClient.close();

	}

}
