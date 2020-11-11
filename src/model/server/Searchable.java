package model.server;

import java.util.List;

public interface Searchable<T> {
	
	State<T> getInitialState();
	Boolean isGoalState(State<T> s);
	List<State<T>> getAllPossibleStates(State<T> s);
}
