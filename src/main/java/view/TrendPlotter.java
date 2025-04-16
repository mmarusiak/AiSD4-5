package view;

import controllers.FieldAvgLinker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class TrendPlotter extends JFrame {

    public TrendPlotter(String title, FieldAvgLinker.DataSet[] datasets, String graphTitle, String yLabel) {
        super(title);
        JPanel chartPanel = createChartPanel(datasets, graphTitle, yLabel);
        setContentPane(chartPanel);
    }

    private JPanel createChartPanel(FieldAvgLinker.DataSet[] datasets, String title, String yLabel) {
        YIntervalSeriesCollection scatterDataset = new YIntervalSeriesCollection();
        XYPlot plot;

        // Create empty chart to attach datasets
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Zależność rozmiaru wejścia a " + title,
                "Rozmiar wejścia",
                "Średnia wartość " + yLabel,
                scatterDataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        plot = chart.getXYPlot();

        XYErrorRenderer errorRenderer = new XYErrorRenderer();
        errorRenderer.setDrawYError(true);
        plot.setRenderer(0, errorRenderer);

        plot.setDatasetRenderingOrder(org.jfree.chart.plot.DatasetRenderingOrder.FORWARD);
        int trendDatasetIndex = 1;

        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE};
        String polymonialText = "";

        for (int idx = 0; idx < datasets.length; idx++) {
            FieldAvgLinker.DataSet data = datasets[idx];
            double[] x = data.getX();
            double[] y = data.getY();
            double[] error = data.getStandardDeviation();
            String label = data.getLabel();
            int degree = data.getDegree();

            // Wybór koloru dla tej serii
            Color seriesColor = colors[idx % colors.length];

            // Dodanie danych punktowych (z błędami - odchylenie standardowe)
            YIntervalSeries scatterSeries = new YIntervalSeries(label);
            for (int i = 0; i < x.length; i++) {
                scatterSeries.add(x[i], y[i], y[i] - error[i], y[i] + error[i]);
            }
            scatterDataset.addSeries(scatterSeries);
            errorRenderer.setSeriesPaint(idx, seriesColor); // kolor punktów

            // Obliczenie funkcji kwadratowej, najlepszego dopasowania - linia trendu
            double[] coeffs = fitPolynomial(x, y, degree);

            // Rysowanie lini trendu
            XYSeries trendSeries = new XYSeries("Trend: " + label + " (stopień" + degree + ")");
            double minX = x[0], maxX = x[x.length - 1];
            double step = (maxX - minX) / 100.0;
            for (int i = 0; i <= 100; i++) {
                double xi = minX + i * step;
                trendSeries.add(xi, evalPoly(coeffs, xi));
            }

            XYSeriesCollection trendDataset = new XYSeriesCollection(trendSeries);
            plot.setDataset(trendDatasetIndex, trendDataset);

            // Renderer dla linii trendu z tym samym kolorem
            XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
            lineRenderer.setSeriesLinesVisible(0, true);
            lineRenderer.setSeriesShapesVisible(0, false);
            lineRenderer.setSeriesPaint(0, seriesColor); // kolor linii = kolor punktów
            plot.setRenderer(trendDatasetIndex, lineRenderer);

            // Adnotacja z równaniem i R^2
            double rSquared = calculateRSquared(x, y, coeffs);
            String formula = getPolynomialFormula(coeffs);
            polymonialText += "\n" + label + ": " + formula + " (R² = " + new DecimalFormat("#.###").format(rSquared) + ")";


            trendDatasetIndex++;
        }

        // Dodanie adnotacji do wykresu
        FieldAvgLinker.Tools.addMultilineTextAnnotations(plot.getDomainAxis().getRange().getLength() * .25, plot.getRangeAxis().getRange().getLength() * .9, polymonialText, Color.BLACK, plot);

        return new ChartPanel(chart);
    }

    // Lagrange basis Polynomial https://en.wikipedia.org/wiki/Lagrange_polynomial
    // https://www.youtube.com/watch?v=bzp_q7NDdd4
    private static double[] fitPolynomial(double[] x, double[] y, int degree) {
        int n = x.length;
        double[] result = new double[degree + 1];
        double[][] X = new double[degree + 1][degree + 1];
        double[] Y = new double[degree + 1];

        for (int i = 0; i <= degree; i++) {
            for (int j = 0; j <= degree; j++) {
                for (int k = 0; k < n; k++) {
                    X[i][j] += Math.pow(x[k], i + j);
                }
            }
            for (int k = 0; k < n; k++) {
                Y[i] += y[k] * Math.pow(x[k], i);
            }
        }

        for (int i = 0; i <= degree; i++) {
            double factor = X[i][i];
            for (int j = 0; j <= degree; j++) X[i][j] /= factor;
            Y[i] /= factor;

            for (int k = 0; k <= degree; k++) {
                if (k == i) continue;
                double ratio = X[k][i];
                for (int j = 0; j <= degree; j++) {
                    X[k][j] -= ratio * X[i][j];
                }
                Y[k] -= ratio * Y[i];
            }
        }

        for (int i = 0; i <= degree; i++) result[i] = Y[i];
        return result;
    }

    private static double evalPoly(double[] coeffs, double x) {
        double result = 0;
        for (int i = 0; i < coeffs.length; i++) {
            result += coeffs[i] * Math.pow(x, i);
        }
        return result;
    }

    private static double calculateRSquared(double[] x, double[] y, double[] coeffs) {
        double ssRes = 0, ssTot = 0, mean = 0;
        for (double v : y) mean += v;
        mean /= y.length;
        for (int i = 0; i < x.length; i++) {
            double pred = evalPoly(coeffs, x[i]);
            ssRes += Math.pow(y[i] - pred, 2);
            ssTot += Math.pow(y[i] - mean, 2);
        }
        return 1 - ssRes / ssTot;
    }

    private static String getPolynomialFormula(double[] coeffs) {
        DecimalFormat df = new DecimalFormat("#.#######");
        StringBuilder sb = new StringBuilder();
        for (int i = coeffs.length - 1; i >= 0; i--) {
            if (i != coeffs.length - 1) sb.append(" + ");
            sb.append(df.format(coeffs[i]));
            if (i > 0) sb.append("x");
            if (i > 1) sb.append("^").append(i);
        }
        return superscript(sb.toString());
    }

    // https://stackoverflow.com/a/12837524/13786856
    public static String superscript(String str) {
        str = str.replaceAll("\\^0", "⁰");
        str = str.replaceAll("\\^1", "¹");
        str = str.replaceAll("\\^2", "²");
        str = str.replaceAll("\\^3", "³");
        str = str.replaceAll("\\^4", "⁴");
        str = str.replaceAll("\\^5", "⁵");
        str = str.replaceAll("\\^6", "⁶");
        str = str.replaceAll("\\^7", "⁷");
        str = str.replaceAll("\\^8", "⁸");
        str = str.replaceAll("\\^9", "⁹");
        return str;
    }


    public static void drawGraph(FieldAvgLinker.DataSet[] datasets, String title, String yLabel) {
        SwingUtilities.invokeLater(() -> {
            TrendPlotter chart = new TrendPlotter("Wykres AiSD", datasets, title, yLabel);
            chart.setSize(900, 600);
            chart.setLocationRelativeTo(null);
            chart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            chart.setVisible(true);
        });
    }
}
