package com.B2BMarket.ordering.application.checkout;

import com.B2BMarket.ordering.domain.model.commons.ZipCode;
import com.B2BMarket.ordering.domain.model.order.BillingInfo;
import com.B2BMarket.ordering.domain.model.order.CheckoutService;
import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.order.Orders;
import com.B2BMarket.ordering.domain.model.order.PaymentMethod;
import com.B2BMarket.ordering.domain.model.order.Shipping;
import com.B2BMarket.ordering.domain.model.order.shipping.OriginAddressService;
import com.B2BMarket.ordering.domain.model.order.shipping.ShippingCostService;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCarts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CheckoutApplicationService {

    private final CheckoutService checkoutService;
    private final ShoppingCarts shoppingCarts;

    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;

    private final Orders orders;

    private final ShippingInputDisassembler shippingInputDisassembler;
    private final BillingInputDisassembler billingInputDisassembler;

    @Transactional
    public String checkout(CheckoutInput input){
        Objects.requireNonNull(input);

        PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());

        ShoppingCartId shoppingCartId = new ShoppingCartId(input.getShoppingCartId());
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));

        var shippingCalculationResult = calculateShippingCost(input.getShipping());

        Shipping shipping = shippingInputDisassembler.toDomainModel(input.getShipping(), shippingCalculationResult);

        BillingInfo billing = billingInputDisassembler.toDomainModel(input.getBilling());

        Order order = checkoutService.checkout(shoppingCart, billing, shipping, paymentMethod);

        orders.add(order);
        shoppingCarts.add(shoppingCart);

        return order.id().toString();
    }

    private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shipping) {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode(shipping.getAddress().getZipCode());
        return shippingCostService.calculate(new ShippingCostService.CalculationRequest(
                origin,
                destination
        ));
    }

}
