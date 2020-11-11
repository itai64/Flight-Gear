package model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;


public class MyTestClientHandler implements ClientHandler {
	private Solver<String, String> solver;
	private CacheManager<String, String> cm;

	String exitstr = "end";

	public MyTestClientHandler() {
		this.solver = new StringReverser();
		this.cm = new FileCacheManager<>();
	}

	public MyTestClientHandler(MyTestClientHandler other) {
		this.solver = other.solver;
		this.cm = other.cm;
	}

	public void handelClient(InputStream in, OutputStream out) {

		BufferedReader userInput = new BufferedReader(new InputStreamReader(in));
		PrintWriter pw = new PrintWriter(out);
		String line = "";

		try {
			while ((line = userInput.readLine()) != null) {
				if (line.equals(exitstr))
					return;
			String sol;
			if (cm.isexist(line)) {
				pw.println(cm.querySolution(line));
			} else {
				
				cm.saveSolution(line,sol = solver.solve(line));
				pw.println(sol);
			}
			pw.flush();}
		}

		catch (IOException e) {
			System.out.println();
			e.printStackTrace();
		}
	}

}
