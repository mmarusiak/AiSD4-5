package controllers.core;

import java.util.Comparator;
import java.util.List;

import controllers.measuring.CountingSwapper;

public abstract class AbstractSwappingSortingAlgorithm<T> extends AbstractSortingAlgorithm<T> {

    private CountingSwapper<T> swapper;

    public AbstractSwappingSortingAlgorithm(Comparator<? super T> comparator) {
        super(comparator);

        swapper = new CountingSwapper<T>();
    }

    @Override
    public void reset() {
        super.reset();
        swapper.reset();
    }

    public long swaps() {
        return swapper.count();
    }

    protected void swap(List<T> list, int index1, int index2) {
        swapper.swap(list, index1, index2);
    }
}
