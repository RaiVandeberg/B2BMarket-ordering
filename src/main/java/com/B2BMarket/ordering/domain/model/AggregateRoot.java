package com.B2BMarket.ordering.domain.model;

public interface AggregateRoot<ID> extends DomainEventSource {
    ID id();
}
