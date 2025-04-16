package controllers.testing.results;

public class RunResult {

	private long timeMillis;
	
	private long comps;
	
	private boolean srted;
	private boolean stble;
	
	public RunResult(long timeMillis, long comparisons, boolean sorted, boolean stable) {
		this.timeMillis = timeMillis;
		this.comps = comparisons;
		this.srted = sorted;
		this.stble = stable;
	}
	
	public long timeInMilliseconds() {
		return timeMillis;
	}
	
	public long comparisons() {
		return comps;
	}
	
	public boolean sorted() {
		return srted;
	}
	
	public boolean stable() {
		return stble;
	}
}
