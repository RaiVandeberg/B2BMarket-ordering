package com.B2BMarket.ordering.application.shoppingcart.management;

import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.CustomerId;
import com.B2BMarket.ordering.domain.model.product.Product;
import com.B2BMarket.ordering.domain.model.product.ProductCatalogService;
import com.B2BMarket.ordering.domain.model.product.ProductId;
import com.B2BMarket.ordering.domain.model.product.ProductNotFoundException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCart;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.B2BMarket.ordering.domain.model.shoppingcart.ShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {

    private final ShoppingCarts shoppingCarts;
    private final ProductCatalogService productCatalogService;
    private final ShoppingService shoppingService;

    @Transactional
    public void addItem(ShoppingCartItemInput input) {
        Objects.requireNonNull(input);

        ShoppingCartId shoppingCartId = new ShoppingCartId(input.getShoppingCartId());
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));

        ProductId productId = new ProductId(input.getProductId());
        Product product = productCatalogService.ofId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        shoppingCart.addItem(product, new Quantity(input.getQuantity()));

        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public UUID createNew(UUID rawCustomerId) {
        Objects.requireNonNull(rawCustomerId);

        CustomerId customerId = new CustomerId(rawCustomerId);

        ShoppingCart shoppingCart = shoppingService.startShopping(customerId);

        shoppingCarts.add(shoppingCart);

        return shoppingCart.id().value();
    }

    @Transactional
    public void removeItem(UUID rawShoppingCartId, UUID rawShoppingCartItemId) {
        Objects.requireNonNull(rawShoppingCartId);
        Objects.requireNonNull(rawShoppingCartItemId);

        ShoppingCartId shoppingCartId = new ShoppingCartId(rawShoppingCartId);
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));

        shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));

        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void empty(UUID rawShoppingCartId) {
        Objects.requireNonNull(rawShoppingCartId);

        ShoppingCartId shoppingCartId = new ShoppingCartId(rawShoppingCartId);
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));

        shoppingCart.empty();

        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void delete(UUID rawShoppingCartId) {
        Objects.requireNonNull(rawShoppingCartId);

        ShoppingCartId shoppingCartId = new ShoppingCartId(rawShoppingCartId);
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));

        shoppingCarts.remove(shoppingCart);
    }

}
