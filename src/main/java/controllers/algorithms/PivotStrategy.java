package controllers.algorithms;

import java.util.List;

public interface PivotStrategy <T>{
    T choosePivot(List<T> list);
}
