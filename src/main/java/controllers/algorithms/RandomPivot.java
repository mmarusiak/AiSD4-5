package controllers.algorithms;

import java.util.List;
import java.util.Random;

public class RandomPivot<T> implements PivotStrategy<T> {
    private final Random rand = new Random();

    @Override
    public T choosePivot(List<T> list) {
        return list.get(rand.nextInt(list.size()));
    }
}
