package controllers;

import controllers.core.AbstractSortingAlgorithm;
import controllers.core.AbstractSwappingSortingAlgorithm;
import controllers.testing.MarkedValue;
import controllers.testing.Tester;
import controllers.testing.generation.Generator;
//import controllers.testing.results.swapping.Result;
import controllers.testing.results.Result;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;
import java.util.function.Function;

public class FieldAvgLinker {
    private final Function<Result, Double> average;
    private final Function<Result, Double> stddev;

    public FieldAvgLinker(Function<Result, Double> average, Function<Result, Double> stddev) {
        this.average = average;
        this.stddev = stddev;
    }

    public double getAverage(Result result) {
        return average.apply(result);
    }

    public double getStddev(Result result) {
        return stddev.apply(result);
    }

    public static class DataSet {

        private String label;
        private int degree;

        private double[] x;
        private double[] y;
        private double[] standardDeviation;

        public DataSet(String label, int degree, double[] x, double[] y, double[] stddev) {
            this.label = label;
            this.degree = degree;
            this.x = x;
            this.y = y;
            this.standardDeviation = stddev;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getDegree() {
            return degree;
        }

        public void setDegree(int degree) {
            this.degree = degree;
        }

        public double[] getX() {
            return x;
        }

        public void setX(double[] x) {
            this.x = x;
        }

        public double[] getY() {
            return y;
        }

        public void setY(double[] y) {
            this.y = y;
        }

        public double[] getStandardDeviation() {
            return standardDeviation;
        }

        public void setStandardDeviation(double[] standardDeviation) {
            this.standardDeviation = standardDeviation;
        }

    }

    public static class Tools {

        public static void addMultilineTextAnnotations(double x, double y, String label, Color color, XYPlot plot) {
            // Compute a dynamic vertical step. Adjust the percentage if needed.
            double range = plot.getRangeAxis().getRange().getLength();
            double yStep = range * 0.05; // 1% of the total range

            String[] lines = label.split("\n");
            for (int i = 0; i < lines.length; i++) {
                // Each new line is offset by yStep; adjust the multiplier to change spacing
                XYTextAnnotation annotationLabel = new XYTextAnnotation(lines[i], x, y - i * yStep);
                annotationLabel.setPaint(color);
                plot.addAnnotation(annotationLabel);
            }
        }


        public static<A extends AbstractSortingAlgorithm<MarkedValue<T>>, T> DataSet[] dataSetsFromAlgorithm(A algorithm, Generator<MarkedValue<T>> generator,
                                                                                            int reps, int maxSize, int step, FieldAvgLinker... fields){
            DataSet[] dataSets = new DataSet[fields.length];

            int size = maxSize/step;
            double[][] x = new double[fields.length][size];
            double[][] y = new double[fields.length][size];
            double[][] dev = new double[fields.length][size];
            for (int i = 0; i < size; i++) {
                int s = i * step;
                Result result = Tester.runNTimes(algorithm, generator, s, reps);
                for (int j = 0; j < fields.length; j++) {
                    double avg = fields[j].getAverage(result);
                    double stddev = fields[j].getStddev(result);
                    x[j][i] = s;
                    y[j][i] = avg;
                    dev[j][i] = stddev;
                }
            }
            for (int i = 0; i < fields.length;  dataSets[i] = new DataSet(algorithm.getClass().getSimpleName(), 2, x[i], y[i], dev[i]), i++);
            return dataSets;
        }


        public static<A extends AbstractSortingAlgorithm<MarkedValue<T>>, T> DataSet dataSetFromAlgorithm(A algorithm, Generator<MarkedValue<T>> generator,
                                                      Function<Result, Double> avgField, Function<Result, Double> devField, int reps, int maxSize, int step) {
            int size = maxSize/step;
            double[] x = new double[size];
            double[] y = new double[size];
            double[] dev = new double[size];
            for (int i = 0; i < size; i++) {
                int s = i * step;
                Result result = Tester.runNTimes(algorithm, generator, s, reps);
                double avg = avgField.apply(result);
                double stddev = devField.apply(result);
                System.out.println(s);
                x[i] = s;
                y[i] = avg;
                dev[i] = stddev;
            }
            return new DataSet(algorithm.getClass().getSimpleName(), 2, x, y, dev);
        }
    }
}
