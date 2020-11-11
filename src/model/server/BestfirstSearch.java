package model.server;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class BestfirstSearch<T> extends CommonSearcher<T> {

	private static int counter = 0;

	public BestfirstSearch() {
	}


	@Override
	public List<State<T>> Search(Searchable<T> se) {
		open = new PriorityQueue<State<T>>();
		close = new HashMap<>();

		List<State<T>> succesors = null;
		State<T> node = null;

		this.open.add(se.getInitialState());
		initialize();
		while (!open.isEmpty()) {
			node = this.pollFromOpen();
			close.put(node.toString(), node);

			getNumberOfNodesEvaluated();
			if (se.isGoalState(node))
				return node.backTrace();

			succesors = se.getAllPossibleStates(node);

			for (State<T> succesor : succesors) {
				if (!open.contains(succesor) && !close.containsKey(succesor.toString())) {
					open.add(succesor);
				} else {
					if (!close.containsKey(succesor.toString())) {
						State<T> tmp = SpecificPoll(succesor);
						if (tmp.getCost() > succesor.getCost())
							open.add(succesor);
						else
							open.add(tmp);
					}

				}
			}
		}
		return null;
	}

	@Override
	public int getNumberOfNodesEvaluated() {
		counter++;
		return 0;
	}

	public State<T> SpecificPoll(State<T> state) {
		State<T> retVal = null;
		for (State<T> s : open) {
			if (s.equals(state)) {
				retVal = s;
				open.remove(s);
				return retVal;
			}
		}
		return retVal;

	}

}
