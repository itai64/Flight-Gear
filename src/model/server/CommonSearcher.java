package model.server;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public abstract class CommonSearcher<T> implements Searcher<T> {

	private int evaluatedNodes;
	protected PriorityQueue<State<T>> open;
	protected HashMap<String,State<T>> close;

	public CommonSearcher() {
		evaluatedNodes = 0;
	}
	
	protected State<T> pollFromOpen() {			
		evaluatedNodes++;
		return open.poll();
		
	}
	public int getAmountOfevaluatedNodes() {
		return evaluatedNodes;
	}

	public abstract List<State<T>> Search(Searchable<T> se);

	public void initialize() {
		evaluatedNodes = 0;
	};
}
