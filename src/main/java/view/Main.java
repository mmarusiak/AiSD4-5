package view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import controllers.FieldAvgLinker;
import controllers.algorithms.*;
import controllers.core.AbstractSortingAlgorithm;
import controllers.testing.*;
import controllers.testing.comparators.*;
import controllers.testing.generation.*;
import controllers.testing.generation.conversion.*;
//import controllers.testing.results.swapping.Result;
import controllers.testing.results.Result;

public class Main {

	public static void main(String[] args) {
		Generator<MarkedValue<Integer>> generator = new MarkingGenerator<Integer>(new OrderedIntegerArrayGenerator());
		generator = new LinkedListGenerator<>(generator);
		//Generator<MarkedValue<Integer>> generator = new MarkingGenerator<Integer>(new ReversedIntegerArrayGenerator());
		//Generator<MarkedValue<Integer>> generator = new MarkingGenerator<Integer>(new );

		AbstractSortingAlgorithm<MarkedValue<Integer>> alg = new MergeSort3Way<MarkedValue<Integer>>(new MarkedValueComparator<Integer>(new IntegerComparator()));
		//zawiszaVoid(alg, generator);

		graphVoid(generator, 20, 1_000_000, 100_000);
	}


	private static void graphVoid(Generator<MarkedValue<Integer>> generator, int repetitions, int maxSize, int stepSize) {
		Comparator<MarkedValue<Integer>> markedComparator = new MarkedValueComparator<Integer>(new IntegerComparator());

		AbstractSortingAlgorithm<MarkedValue<Integer>>[] algs = new AbstractSortingAlgorithm[] {
				//new QuickSort<MarkedValue<Integer>>(markedComparator, new FirstPivot<MarkedValue<Integer>>()),
				//new ShakerSort<MarkedValue<Integer>>(markedComparator),
				new QuickSort<MarkedValue<Integer>>(markedComparator, new RandomPivot<>()),
				new MergeSort3Way<MarkedValue<Integer>>(markedComparator)
		};
		testAlgorithmsOptimal(generator, algs, repetitions, maxSize, stepSize);
	}


	private static void zawiszaVoid(AbstractSortingAlgorithm<MarkedValue<Integer>> algorithm, Generator<MarkedValue<Integer>> generator) {
		controllers.testing.results.Result result = Tester.runNTimes(algorithm, generator, 1000, 50);

		List<MarkedValue<Integer>> toSort = new ArrayList<>();
		toSort.add(new MarkedValue<>(20));
		toSort.add(new MarkedValue<>(10));
		toSort.add(new MarkedValue<>(90));
		toSort.add(new MarkedValue<>(1));
		toSort.add(new MarkedValue<>(3));
		toSort.add(new MarkedValue<>(10));

		toSort = algorithm.sort(toSort);

		for (MarkedValue<Integer> markedValue : toSort) {
			System.out.println(markedValue.value());
		}

		printStatistic("time [ms]", result.averageTimeInMilliseconds(), result.timeStandardDeviation());
		printStatistic("comparisons", result.averageComparisons(), result.comparisonsStandardDeviation());
		//printStatistic("swaps", result.averageSwaps(), result.swapsStandardDeviation());

		System.out.println("always sorted: " + result.sorted());
		System.out.println("always stable: " + result.stable());
	}


	private static<A extends AbstractSortingAlgorithm<MarkedValue<T>>, T> void testAlgorithms(Generator<MarkedValue<T>> generator, A[] algorithms, int repetitions, int maxSize, int stepSize) {
		FieldAvgLinker.DataSet[] swapsDatasets = new FieldAvgLinker.DataSet[algorithms.length];
		FieldAvgLinker.DataSet[] timeDatasets = new FieldAvgLinker.DataSet[algorithms.length];
		FieldAvgLinker.DataSet[] comparisonsDatasets = new FieldAvgLinker.DataSet[algorithms.length];

		for (int i = 0; i < algorithms.length; i++) {
			A alg = algorithms[i];
			comparisonsDatasets[i] = FieldAvgLinker.Tools.dataSetFromAlgorithm(alg, generator, Result::averageComparisons, Result::comparisonsStandardDeviation, repetitions, maxSize, stepSize);
			//swapsDatasets[i] = FieldAvgLinker.Tools.dataSetFromAlgorithm(alg, generator, Result::averageSwaps, Result::swapsStandardDeviation, repetitions, maxSize, stepSize);
			timeDatasets[i] = FieldAvgLinker.Tools.dataSetFromAlgorithm(alg, generator, Result::averageTimeInMilliseconds,Result::timeStandardDeviation, repetitions, maxSize, stepSize);
		}

		TrendPlotter.drawGraph(swapsDatasets, "zamianami", "");
		TrendPlotter.drawGraph(timeDatasets, "czasem", "[ms]");
		TrendPlotter.drawGraph(comparisonsDatasets, "porównaniami", "");
	}


	private static<A extends AbstractSortingAlgorithm<MarkedValue<T>>, T> void testAlgorithmsOptimal(Generator<MarkedValue<T>> generator,A[] algorithms, int repetitions, int maxSize, int stepSize) {
		//FieldAvgLinker.DataSet[] swapsDatasets = new FieldAvgLinker.DataSet[algorithms.length];
		FieldAvgLinker.DataSet[] timeDatasets = new FieldAvgLinker.DataSet[algorithms.length];
		FieldAvgLinker.DataSet[] comparisonsDatasets = new FieldAvgLinker.DataSet[algorithms.length];

		for (int i = 0; i < algorithms.length; i++) {
			A alg = algorithms[i];
			/*FieldAvgLinker.DataSet[] algSets = FieldAvgLinker.Tools.dataSetsFromAlgorithm(alg, generator, repetitions, maxSize, stepSize,
					new FieldAvgLinker(Result::averageSwaps, Result::swapsStandardDeviation),
					new FieldAvgLinker(Result::averageComparisons, Result::comparisonsStandardDeviation),
					new FieldAvgLinker(Result::averageTimeInMilliseconds, Result::timeStandardDeviation));*/
			FieldAvgLinker.DataSet[] algSets = FieldAvgLinker.Tools.dataSetsFromAlgorithm(alg, generator, repetitions, maxSize, stepSize,
					new FieldAvgLinker(Result::averageComparisons, Result::comparisonsStandardDeviation),
					new FieldAvgLinker(Result::averageTimeInMilliseconds, Result::timeStandardDeviation));
			//swapsDatasets[i] = algSets[0];
			comparisonsDatasets[i] = algSets[0];
			timeDatasets[i] = algSets[1];
		}

		//TrendPlotter.drawGraph(swapsDatasets, "zamianami");
		TrendPlotter.drawGraph(timeDatasets, "czasem", "[ms]");
		TrendPlotter.drawGraph(comparisonsDatasets, "porównaniami", "");
	}


	private static void printStatistic(String label, double average, double stdDev) {
		System.out.println(label + ": " + double2String(average) + " +- " + double2String(stdDev));
	}


	private static String double2String(double value) {
		return String.format("%.12f", value);
	}
}
