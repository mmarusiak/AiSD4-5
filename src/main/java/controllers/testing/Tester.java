package controllers.testing;

import java.util.List;

import controllers.core.AbstractSortingAlgorithm;
import controllers.core.AbstractSwappingSortingAlgorithm;

import controllers.measuring.Statistics;
import controllers.measuring.Timer;

import controllers.testing.generation.Generator;
import controllers.testing.results.*;

public class Tester {

	public static <T> controllers.testing.results.swapping.RunResult runOnce(AbstractSwappingSortingAlgorithm<MarkedValue<T>> algorithm,
                                                                             Generator<MarkedValue<T>> generator, int size) {
		return repack(runOnce((AbstractSortingAlgorithm<MarkedValue<T>>) algorithm, generator, size),
					  algorithm.swaps());
	}

	public static <T> RunResult runOnce(AbstractSortingAlgorithm<MarkedValue<T>> algorithm,
										Generator<MarkedValue<T>> generator, int size) {
		algorithm.reset();
		
		List<MarkedValue<T>> list = generator.generate(size);
		Timer timer = new Timer();

		timer.start();
		list = algorithm.sort(list);
		timer.stop();
		
		return new RunResult(timer.durationMillis(), algorithm.comparisons(),
							 ListChecker.isSorted(list, algorithm.baseComparator()),
							 ListChecker.isStable(list, algorithm.baseComparator()));
	}

	public static <T> controllers.testing.results.swapping.Result runNTimes(AbstractSwappingSortingAlgorithm<MarkedValue<T>> algorithm,
                                                                            Generator<MarkedValue<T>> generator,
                                                                            int size, int repetitions) {
		Statistics time = new Statistics();
		Statistics comparisons = new Statistics();
		Statistics swaps = new Statistics();

		boolean sorted = true;
		boolean stable = true;

		for(int n = 0; n < repetitions; ++n) {
			controllers.testing.results.swapping.RunResult result = runOnce(algorithm, generator, size);

			time.update(result.timeInMilliseconds());
			comparisons.update(result.comparisons());
			swaps.update(result.swaps());

			sorted = sorted && result.sorted();
			stable = stable && result.stable();
		}

		return new controllers.testing.results.swapping.Result(time.average(), time.standardDeviation(),
												   comparisons.average(), comparisons.standardDeviation(),
												   swaps.average(), swaps.standardDeviation(),
												   sorted, stable);
	}

	public static <T> Result runNTimes(AbstractSortingAlgorithm<MarkedValue<T>> algorithm,
									   Generator<MarkedValue<T>> generator, int size, int repetitions) {
		Statistics time = new Statistics();
		Statistics comparisons = new Statistics();
		
		boolean sorted = true;
		boolean stable = true;
		
		for(int n = 0; n < repetitions; ++n) {
			RunResult result = runOnce(algorithm, generator, size);

			time.update(result.timeInMilliseconds());
			comparisons.update(result.comparisons());
			
			sorted = sorted && result.sorted();
			stable = stable && result.stable();
		}
		
		return new Result(time.average(), time.standardDeviation(),
						  comparisons.average(), comparisons.standardDeviation(),
						  sorted, stable);
	}

	private static controllers.testing.results.swapping.RunResult repack(RunResult result, long swaps) {
		return new controllers.testing.results.swapping.RunResult(result.timeInMilliseconds(), result.comparisons(), swaps,
													  result.sorted(), result.stable());
	}
}
