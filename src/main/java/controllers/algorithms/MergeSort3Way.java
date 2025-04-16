package controllers.algorithms;

import controllers.core.AbstractSortingAlgorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort3Way<T> extends AbstractSortingAlgorithm<T> {

    public MergeSort3Way(Comparator<T> comparator) {
        super(comparator);
    }
    @Override
    public List<T> sort(List<T> list) {
        if (list.size() <= 1) return list;

        int third = list.size() / 3;
        if (third == 0) {
            return fallbackMergeSort(list);
        }

        List<T> left   = new ArrayList<>(list.subList(0, third));
        List<T> middle = new ArrayList<>(list.subList(third, 2 * third));
        List<T> right  = new ArrayList<>(list.subList(2 * third, list.size()));

        left = sort(left);       // REKURSYWNIE sortuj i przypisz wynik
        middle = sort(middle);
        right = sort(right);

        return mergeThree(left, middle, right); // zwróć nową, posortowaną listę
    }


    private List<T> mergeThree(List<T> a, List<T> b, List<T> c) {
        List<T> merged = new ArrayList<>();
        int i = 0, j = 0, k = 0;

        while (i < a.size() || j < b.size() || k < c.size()) {
            T ai = i < a.size() ? a.get(i) : null;
            T bj = j < b.size() ? b.get(j) : null;
            T ck = k < c.size() ? c.get(k) : null;

            T min = min(ai, bj, ck);
            if (min == null) break;

            if (min.equals(ai)) i++;
            else if (min.equals(bj)) j++;
            else k++;

            merged.add(min);
        }

        return merged;
    }

    private T min(T a, T b, T c) {
        T min = null;

        if (a != null) {
            min = a;
        }

        if (b != null) {
            if (min == null || compare(b, min) < 0) {
                min = b;
            }
        }

        if (c != null) {
            if (min == null || compare(c, min) < 0) {
                min = c;
            }
        }

        return min;
    }

    private List<T> fallbackMergeSort(List<T> list) {
        if (list.size() <= 1) return list;
        int mid = list.size() / 2;
        List<T> left = sort(new ArrayList<>(list.subList(0, mid)));
        List<T> right = sort(new ArrayList<>(list.subList(mid, list.size())));
        return mergeThree(left, right, new ArrayList<>());
    }
}
