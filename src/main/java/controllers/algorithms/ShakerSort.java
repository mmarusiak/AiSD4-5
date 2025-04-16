package controllers.algorithms;

import controllers.core.AbstractSwappingSortingAlgorithm;

import java.util.Comparator;
import java.util.List;

public class ShakerSort<T> extends AbstractSwappingSortingAlgorithm<T> {
    public ShakerSort(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {
        int start = 0, end = list.size() - 1;
        int currentPos = start;
        int step = 1;

        boolean changes;
        do{
            changes = false;
            while (moreElementsToCompare(step, currentPos, start, end)){
                if (shouldSwap(step, currentPos, list)) {
                    swap(list, currentPos, currentPos + step);
                    changes = true;
                }
                currentPos += step;
            }
            if (step == 1) end = currentPos - 1;
            else start = currentPos + 1;
            step *= -1;
        }
        while (changes);

        return list;
    }

    private boolean moreElementsToCompare(int step, int currentPos, int start, int end){
        return (step == 1 && currentPos < end) || (step == -1 && currentPos > start);
    }

    private boolean shouldSwap(int step, int currentPos, List<T> list){
        T a = list.get(currentPos);
        T b = list.get(currentPos + step);
        return (compare(a, b) > 0 && step == 1) || (compare(b, a) > 0 && step == -1);
    }
}
