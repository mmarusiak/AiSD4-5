package controllers.testing.results;

public class Result {

	private double avgTime;
	private double timeStdDev;
	
	private double avgComparisons;
	private double comparisonsStdDev;
	
	private boolean srted;
	private boolean stble;
	
	public Result(double avgTime, double timeStdDev, double avgComparisons, double comparisonsStdDev, 
			      boolean sorted, boolean stable) {
		this.avgTime = avgTime;
		this.timeStdDev = timeStdDev;
		
		this.avgComparisons = avgComparisons;
		this.comparisonsStdDev = comparisonsStdDev;
		
		this.srted = sorted;
		this.stble = stable;
	}
	
	public double averageTimeInMilliseconds() {
		return avgTime;
	}
	
	public double timeStandardDeviation() {
		return timeStdDev;
	}
	
	public double averageComparisons() {
		return avgComparisons;
	}
	
	public double comparisonsStandardDeviation() {
		return comparisonsStdDev;
	}
	
	public boolean sorted() {
		return srted;
	}
	
	public boolean stable() {
		return stble;
	}
}
