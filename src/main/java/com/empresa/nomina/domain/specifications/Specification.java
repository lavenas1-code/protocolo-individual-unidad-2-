package com.empresa.nomina.domain.specifications;

public interface Specification<T> {
    boolean isSatisfiedBy(T candidate);
}
