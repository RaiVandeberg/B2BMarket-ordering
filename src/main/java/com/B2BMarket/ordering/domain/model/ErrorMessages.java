package com.B2BMarket.ordering.domain.model;
import com.B2BMarket.ordering.domain.model.commons.Email;
import com.B2BMarket.ordering.domain.model.commons.FullName;
import com.B2BMarket.ordering.domain.model.commons.Quantity;
import com.B2BMarket.ordering.domain.model.customer.BirthDate;
import com.B2BMarket.ordering.domain.model.customer.Customer;
import com.B2BMarket.ordering.domain.model.order.Order;
import com.B2BMarket.ordering.domain.model.product.Product;

public class ErrorMessages {
    public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "Email is Invalid";
    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";
    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";
    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";

    public static final String ERROR_CUSTOMER_ARCHIVED = "Customer is archived it cannot be changed";

    public static final String ERROR_ORDER_STATUS_CANNOT_BE_CHANGED = "Cannot change order %s status from %s to %s";
    public static final String ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST
            = "Order %s expected delivery date cannot be in the past";
    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS
            = "Order %s cannot be closed, it has no items";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_SHIPPING_INFO
            = "Order %s cannot be placed, it has no shipping info";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_BILLING_INFO
            = "Order %s cannot be placed, it has no billing info";

    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_PAYMENT_METHOD
            = "Order %s cannot be placed, it has no payment method";

    public static final String ERROR_ORDER_DOES_NOT_CONTAIN_ITEM
            = "Order %s does not contain item %s";

    public  static  final String ERROR_PRODUCT_IS_OUT_OF_STOCK =
            "Product %s is out of stock";

    public static final String ERROR_ORDER_CANNOT_BE_EDITED =
            "Order %s with status %s cannot be edited";


    public static final String ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM
            = "Shopping cart %s does not contain item %s";

    public static final String ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT
            = "Shopping cart %s does not contain an item for the product %s";

    public static final String ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT
            = "Shopping cart item %s is incompatible with the product %s";

    public static final String VALIDATION_ERROR_QUANTITY_MUST_BE_GREATER_THAN_ZERO
            = "Quantity must be greater than zero";

    public static final String ERROR_CANT_ADD_LOYALTY_POINTS_ORDER_IS_NOT_READY
            = "Can't add loyalty points because the order is not ready";

    public static final String ERROR_CUSTOMER_EMAIL_IS_IN_USE
            = "Customer email is already in use";

    public static final String ERROR_ORDER_NOT_BELONGS_TO_CUSTOMER
            = "Order does not belong to this customer";

    public static final String ERROR_SHOPPING_CART_CANT_PROCEED_TO_CHECKOUT
            = "Shopping cart cannot proceed to checkout, it is empty or contains unavailable items";

    public static final String ERROR_CUSTOMER_NOT_FOUND
            = "Customer %s not found";

    public static final String ERROR_CUSTOMER_ALREADY_HAVE_SHOPPING_CART
            = "Customer %s already has a shopping cart";

}
