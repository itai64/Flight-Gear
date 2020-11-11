package model.server;


public interface CacheManager<Problem, Solution> {

	public Boolean isexist(Problem p);
	public void saveSolution(Problem p, Solution s);
	public Solution querySolution(Problem p);
	
}
