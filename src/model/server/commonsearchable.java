package model.server;

import java.util.List;
import java.util.ArrayList;

public class commonsearchable implements Searchable<Position> {

	double[][] costsmat;
	Position initailstat;
	Position goalState;

	public commonsearchable(double[][] mat, Position start, Position goal) {
		costsmat = mat;
		initailstat = start;
		goalState = goal;
	}

	/*
	 * This function create and return an initialized state position object
	 * 
	 * @return State<Position>
	 */
	public State<Position> getInitialState() {
		return new State<Position>(initailstat, costsmat[initailstat.getX()][initailstat.getY()], null);
	}

	/*
	 * This function checks if the current state of the object is the same as the
	 * the goal state
	 * 
	 * @param State<Position> State object of position
	 * 
	 * @return boolean if the current state is the goal
	 */
	public Boolean isGoalState(State<Position> s) {
		return goalState.equals(s.getCurrentState());
	}

	/*
	 * This function checks and return a list of all the possible state available
	 * from a given position, by validate the permitted steps in the matrix range.
	 * 
	 * @param State<Position> State object of position
	 * 
	 * @return List<Position> successors
	 */
	public List<State<Position>> getAllPossibleStates(State<Position> s) {
		ArrayList<State<Position>> successors = new ArrayList<State<Position>>();
		List<Position> moves = getMoves(s.getCurrentState());
		for (Position p : moves) {
			successors.add(new State<Position>(p, s.getCost() + costsmat[p.getX()][p.getY()]+ H(p), s));
		}

		return successors;
	}

	/*
	 * This function extract the coordinates from given Position and check what are
	 * the possible moves consider the matrix range
	 * 
	 * @param Position
	 * 
	 * @return List<Position> array of the possible moves to specific position
	 */
	public List<Position> getMoves(Position p) {
		ArrayList<Position> moves = new ArrayList<Position>();
		int x = p.getX();
		int y = p.getY();

		if (validatePosition(x + 1, y))
			moves.add(new Position(x + 1, y));
		if (validatePosition(x - 1, y))
			moves.add(new Position(x - 1, y));
		if (validatePosition(x, y + 1))
			moves.add(new Position(x, y + 1));
		if (validatePosition(x, y - 1))
			moves.add(new Position(x, y - 1));

		return moves;
	}

	/*
	 * This function checks the validation of the position, based on matrix size
	 * 
	 * @return True/False based on the range
	 */
	public boolean validatePosition(int i, int j) {
		return (i >= 0) && (i < costsmat.length) && (j >= 0) && (j < costsmat[i].length);
	}
	
	public double H(Position p){
	return 0.5*Math.sqrt((p.getX() - goalState.getX())*(p.getX() - goalState.getX())+
					(p.getY() - goalState.getY())*(p.getY() - goalState.getY()));
	}

}
