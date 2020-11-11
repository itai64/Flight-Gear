package model.server;

import java.util.List;

public class SearcherToSolver implements Solver<Searchable<Position>, String> {

	private Searcher<Position> solver;

	public SearcherToSolver(Searcher<Position> s) {
		solver = s;
	}

	public String solve(Searchable<Position> p) {
		StringBuilder solution = new StringBuilder();
		List<State<Position>> list = solver.Search(p);
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i).getCurrentState().getX() < list.get(i + 1).getCurrentState().getX())
				solution.append("Down,");
			if (list.get(i).getCurrentState().getX() > list.get(i + 1).getCurrentState().getX())
				solution.append("Up,");
			if (list.get(i).getCurrentState().getY() < list.get(i + 1).getCurrentState().getY())
				solution.append("Right,");
			if (list.get(i).getCurrentState().getY() > list.get(i + 1).getCurrentState().getY())
				solution.append("Left,");

		}
		solution.deleteCharAt(solution.length()-1);
		
		return solution.toString();
	}

}
