package controllers.algorithms;

import controllers.core.AbstractSwappingSortingAlgorithm;

import java.util.Comparator;
import java.util.List;

public class SelectionSort<T> extends AbstractSwappingSortingAlgorithm<T> {

    public SelectionSort(Comparator<T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {
        for (int end = list.size() - 1; end > 0; swap(list, end, getMaxIndex(list, end--)));
        return list;
    }

    private int getMaxIndex(List<T> list, int end) {
        T max = list.getFirst();
        int mIndex = 0;

        for (int i = 1; i <= end; i++) {
            if (compare(max, list.get(i)) < 0) {
                mIndex = i;
                max = list.get(i);
            }
        }
        return mIndex;
    }
}
