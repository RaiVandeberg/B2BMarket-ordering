package com.B2BMarket.ordering.domain.entity;

import com.B2BMarket.ordering.domain.valueObject.*;
import com.B2BMarket.ordering.domain.valueObject.id.CustomerId;
import com.B2BMarket.ordering.domain.valueObject.id.ProductId;

import java.time.LocalDate;

import static javax.print.attribute.standard.JobState.CANCELED;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();

    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

    private Money shippingCost = new Money("10.00");

    private LocalDate expectedDeliveryDate = LocalDate.now().plusWeeks(1);

    private ShippingInfo shippingInfo = aShippingInfo();
    private BillingInfo billingInfo = aBillingInfo();

    private boolean withItems = true;

    private OrderStatus status = OrderStatus.DRAFT;

    private OrderTestDataBuilder() {

    }

    public  static OrderTestDataBuilder anOrder(){
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);
        order.changeBilling(billingInfo);
        order.changePaymentMethod(paymentMethod);

        if (withItems) {
            order.addItem(ProductTestDataBuilder.aProduct().build(),
                    new Quantity(2)
            );

            order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(),
                    new Quantity(1)
            );
        }

        switch (this.status) {
            case DRAFT -> {
            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {
            }
            case CANCELLED -> {
            }
        }

        return order;
    }


    public static BillingInfo aBillingInfo() {
        return BillingInfo.builder()
                .address(anAddress())
                .document(new Document("225-09-1922"))
                .phone(new Phone("123-111-9911"))
                .fullName(new FullName("John", "Smith"))
                .build();
    }

    public static ShippingInfo aShippingInfo() {
        return ShippingInfo.builder()
                .address(anAddress())
                .fullName(new FullName("Rai", "Braz"))
                .document(new Document("111-11-1111"))
                .phone(new Phone(" 111-444-2512"))
                .build();
    }

    public static Address anAddress() {
        return Address.builder()
                .street("Nelson costa")
                .number("1234")
                .neighborhood("Nort Street")
                .complement("House black")
                .city("Berlin")
                .state("Bahia")
                .zipCode(new ZipCode("78487")).build();
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
        return this;
    }

    public OrderTestDataBuilder expectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    public OrderTestDataBuilder shippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
        return this;
    }

    public OrderTestDataBuilder billingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }




}
