package controllers.testing.results.swapping;

public class Result extends controllers.testing.results.Result {

    private double avgSwaps;
    private double swapsStdDev;

    public Result(double avgTime, double timeStdDev, double avgComparisons, double comparisonsStdDev,
                      double avgSwaps, double swapsStdDev, boolean sorted, boolean stable) {
        super(avgTime, timeStdDev, avgComparisons, comparisonsStdDev, sorted, stable);

        this.avgSwaps = avgSwaps;
        this.swapsStdDev = swapsStdDev;
    }

    public double averageSwaps() {
        return avgSwaps;
    }

    public double swapsStandardDeviation() {
        return swapsStdDev;
    }
}
