package controllers.testing.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ShuffledIntegerArrayGenerator implements Generator<Integer> {
	
	private Random rng;
	private OrderedIntegerArrayGenerator integerSequenceGenetator;
	
	public ShuffledIntegerArrayGenerator() {
		rng = new Random();
		integerSequenceGenetator = new OrderedIntegerArrayGenerator();
	}
	
	public ShuffledIntegerArrayGenerator(long seed) {
		rng = new Random(seed);
		integerSequenceGenetator = new OrderedIntegerArrayGenerator();
	}

	@Override
	public ArrayList<Integer> generate(int size) {
		ArrayList<Integer> list = integerSequenceGenetator.generate(size);
		
		Collections.shuffle(list, rng);
		
		return list;
	}
}