package controllers.algorithms;

import controllers.core.AbstractSortingAlgorithm;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class QuickSort<T> extends AbstractSortingAlgorithm<T> {

    private final PivotStrategy<T> pivotStrategy;

    public QuickSort(Comparator<T> comparator, PivotStrategy<T> pivotStrategy) {
        super(comparator);
        this.pivotStrategy = pivotStrategy;
    }

    @Override
    public List<T> sort(List<T> list) {
        return quickSort(list);
    }

    public List<T> quickSort(List<T> list) {
        if (list.size() <= 1) return list;

        T pivot = pivotStrategy.choosePivot(list);
        List<T> less = new LinkedList<>();
        List<T> equal = new LinkedList<>();
        List<T> greater = new LinkedList<>();

        for (T item : list) {
            int cmp = compare(item, pivot);
            if (cmp < 0) less.add(item);
            else if (cmp > 0) greater.add(item);
            else equal.add(item);
        }

        List<T> result = new LinkedList<>();
        if(less.size() > 1) result.addAll(quickSort(less));
        else result.addAll(less);

        result.addAll(equal);
        if (greater.size() > 1) result.addAll(quickSort(greater));
        else result.addAll(greater);

        return result;
    }
}
