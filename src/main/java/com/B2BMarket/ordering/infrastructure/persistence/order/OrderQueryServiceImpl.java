package com.B2BMarket.ordering.infrastructure.persistence.order;

import com.B2BMarket.ordering.application.order.query.OrderDetailOutput;
import com.B2BMarket.ordering.application.order.query.OrderQueryService;
import com.B2BMarket.ordering.application.utility.Mapper;
import com.B2BMarket.ordering.domain.model.order.OrderId;
import com.B2BMarket.ordering.domain.model.order.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderPersistenceEntityRepository repository;
    private final Mapper mapper;

    @Override
    public OrderDetailOutput findById(String id) {
        OrderPersistenceEntity entity = repository.findById(new OrderId(id).value().toLong())
                .orElseThrow(() -> new OrderNotFoundException(new OrderId(id)));
        return mapper.convert(entity, OrderDetailOutput.class);
    }
}
