package com.B2BMarket.ordering.domain.model.repository;

import com.B2BMarket.ordering.domain.model.entity.AggregateRoot;
import com.B2BMarket.ordering.domain.model.entity.Order;

import java.util.Optional;

public interface Repository  <T extends AggregateRoot<ID>, ID> {

    Optional<T> ofId(ID id);
    boolean exists(ID id);
    void add(Order aggregateRoot);
    int count();
}
