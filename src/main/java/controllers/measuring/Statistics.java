package controllers.measuring;

public class Statistics {

    private long n;
    private double avg;
    private double sqrAvg;

    public Statistics() {
        n = 0;
        avg = 0.0;
        sqrAvg = 0.0;
    }

    public double average() {
        return avg;
    }

    public double standardDeviation() {
        return Math.sqrt(sqrAvg - (avg * avg));
    }

    public void update(double value) {
        ++n;
        avg = update(value, avg);
        sqrAvg = update(value * value, sqrAvg);
    }

    private double update(double value, double average) {
        return average + (value - average) / n;
    }
}
