package Utils;

import Abstract.Specifications.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ResultsUtils {
    public static <T> ArrayList<T> filterResults(List<T> set, Specification<T> spec) {
        ArrayList<T> results = new ArrayList<>();
        for(T t : set) {
            if( spec.isSatisfiedBy(t) ) {
                results.add(t);
            }
        }
        return results;
    }
}
