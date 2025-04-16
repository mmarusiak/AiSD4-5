package controllers.algorithms;

import controllers.core.AbstractSwappingSortingAlgorithm;

import java.util.Comparator;
import java.util.List;

public class BinarySearchInsertionSort<T> extends AbstractSwappingSortingAlgorithm<T> {

    public BinarySearchInsertionSort(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {

        for (int i = 0; i < list.size() - 1; i++) {
            moveFromTo(list, i, insertionIndex(list.get(i), list, i - 1));
        }

        return list;
    }

    private void moveFromTo(List<T> list, int fromIndex, int toIndex) {
        int step = fromIndex < toIndex ? 1 : -1;
        int steps = Math.abs(fromIndex - toIndex);

        for (;steps > 0; steps--, swap(list, fromIndex, fromIndex + step), fromIndex += step);
    }

    private int insertionIndex(T targetValue, List<T> targetList, int endIndex){
        if (endIndex < 1){
            return (endIndex == 0 && compare(targetList.getFirst(), targetValue) > 0) ? 1 : 0;
        }

        return binarySearch(targetValue, targetList, 0, endIndex);
    }

    private int binarySearch(T targetValue, List<T> targetList, int startIndex, int endIndex) {
        while (startIndex <= endIndex) {
            int mid = (startIndex + endIndex) / 2;
            if (compare(targetList.get(mid), targetValue) <= 0) {
                endIndex = mid - 1;
            }
            else {
                startIndex = mid + 1;
            }
        }
        return startIndex;
    }
}
