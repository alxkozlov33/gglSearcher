package Abstract.Specifications;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);
}
