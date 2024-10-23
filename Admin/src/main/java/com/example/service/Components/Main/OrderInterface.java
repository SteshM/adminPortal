package com.example.service.Components.Main;

import com.example.dto.CartItemRequest;
import com.example.dto.ExtendedRes;
import com.example.dto.MinimalRes;
import com.example.dto.OrderDto;

public interface OrderInterface {
    public MinimalRes placeOrder(OrderDto orderDto);
    public ExtendedRes getOrderItemDetails(Long orderId);
    public MinimalRes addCartItem(CartItemRequest cartItem);
    public ExtendedRes getCartItems();
    public MinimalRes deleteCartItem(Long cartItemId);
    public ExtendedRes getRetailerAndOrders(Long retailerId);
    public ExtendedRes retailerGetHisOrders();
    public ExtendedRes getDepotOrders(Long depotId);
    public ExtendedRes getMyDepotOrders();
    public ExtendedRes getMyDepotAssignedOrders();
    public ExtendedRes getMyDepotUnAssignedOrders();
    public ExtendedRes getMyCartItemsCount();
    //public Customer confirmOrder();
}
