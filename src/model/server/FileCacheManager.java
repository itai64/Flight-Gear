package model.server;

import java.util.HashMap;

public class FileCacheManager<Problem, Solution> implements CacheManager<Problem, Solution> {

	private HashMap<Problem, Solution> hashsol;

	public FileCacheManager() {
		setCacheMap(new HashMap<Problem, Solution>());
	}

	public void saveSolution(Problem p, Solution s) {
		hashsol.put(p, s);

	}

	public Solution querySolution(Problem p) {
		if (isexist(p))
			hashsol.get(p);
		return this.hashsol.get(p);
	}

	public Boolean isexist(Problem p) {
		return this.hashsol.containsKey(p);
	}

	public void setCacheMap(HashMap<Problem, Solution> cachmap) {
		this.hashsol = cachmap;
	}
}
