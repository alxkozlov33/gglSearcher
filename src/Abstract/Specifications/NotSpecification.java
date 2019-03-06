package Abstract.Specifications;

import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Specification;

public class NotSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec;

    public NotSpecification(Specification<T> s) {
        this.spec=s;
    }

    @Override
    public boolean isSatisfiedBy(T t) {
        return !spec.isSatisfiedBy(t);
    }

}
