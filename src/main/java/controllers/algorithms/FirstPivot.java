package controllers.algorithms;

import java.util.List;

public class FirstPivot<T> implements PivotStrategy<T> {
    @Override
    public T choosePivot(List<T> list){
        return list.getFirst();
    }
}
