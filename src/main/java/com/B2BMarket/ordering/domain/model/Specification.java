package com.B2BMarket.ordering.domain.model;

public interface Specification<T> {
    boolean isSatisfied(T t);

    default Specification<T> and(Specification<T> other) {
        return t -> this.isSatisfied(t) && other.isSatisfied(t);
    }

    default Specification<T> or(Specification<T> other) {
        return t -> this.isSatisfied(t) || other.isSatisfied(t);
    }

    default Specification<T> not() {
        return t -> !this.isSatisfied(t);
    }

    default Specification<T> andNot(Specification<T> other) {
        return t -> this.isSatisfied(t) && !other.isSatisfied(t);
    }

}


