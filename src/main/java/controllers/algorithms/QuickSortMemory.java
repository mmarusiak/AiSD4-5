package controllers.algorithms;

import controllers.core.AbstractSwappingSortingAlgorithm;
import controllers.testing.MarkedValue;

import java.util.Comparator;
import java.util.List;

public class QuickSortMemory<T> extends AbstractSwappingSortingAlgorithm<T> {

    private final PivotStrategy<T> pivotStrategy;

    public QuickSortMemory(Comparator<T> comparator, PivotStrategy<T> pivotStrategy) {
        super(comparator);
        this.pivotStrategy = pivotStrategy;
    }

    @Override
    public List<T> sort(List<T> list) {
        return quickSort(list, 0, list.size() - 1);
    }

    public List<T> quickSort(List<T> list, int low, int high) {
        if (list.size() <= 1 || low >= high) return list;

        int lessIndex = low, greaterIndex = high;
        T pivot = pivotStrategy.choosePivot(list, low, high);
        int i = low;

        while (i <= high)
        {
            T e = list.get(i);
            //System.out.println(i + ": " +  ((MarkedValue)e).value());
            if (compare(pivot, e) > 0 && lessIndex < i) {
                swap(list, i, lessIndex++);
                continue;
            }
            if (compare(pivot, e) < 0 && greaterIndex > i) {
                swap(list, i, greaterIndex--);
                continue;
            }
            i += 1;
        }
        quickSort(list, low, lessIndex);
        quickSort(list, greaterIndex, high);
        return list;
    }
}
