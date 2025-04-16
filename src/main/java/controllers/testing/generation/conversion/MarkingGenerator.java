package controllers.testing.generation.conversion;

import java.util.ArrayList;

import controllers.testing.MarkedValue;
import controllers.testing.generation.Generator;

public class MarkingGenerator<T> implements Generator<MarkedValue<T>> {

	private Generator<? extends T> generator;
	
	public MarkingGenerator(Generator<? extends T> generator) {
		this.generator = generator;
	}
	
	@Override
	public ArrayList<MarkedValue<T>> generate(int size) {
		ArrayList<MarkedValue<T>> list = new ArrayList<MarkedValue<T>>(size);
		
		MarkedValue.clearMarkers();
		for(T value : generator.generate(size)) {
			list.add(new MarkedValue<T>(value));
		}
		
		return list;
	}
}
