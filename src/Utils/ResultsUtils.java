package Utils;

import Abstract.Specifications.Specification;

import java.util.ArrayList;
import java.util.List;

public class ResultsUtils {
    public synchronized static <T> ArrayList<T> filterResults(List<T> set, Specification spec) {
        ArrayList<T> results = new ArrayList<>();
        for(T t : set) {
            if( spec.isSatisfiedBy(t) ) {
                results.add(t);
            }
        }
        return results;
    }
}
