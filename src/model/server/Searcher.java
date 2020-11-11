package model.server;

import java.util.List;

public interface Searcher<T> {

	public abstract List<State<T>> Search(Searchable<T> s);
	
//	for comparisons between algorithms
	public int getNumberOfNodesEvaluated();
}
