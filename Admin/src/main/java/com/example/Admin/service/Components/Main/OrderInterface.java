package com.example.Admin.service.Components.Main;

import com.example.Admin.dto.CartItemRequest;
import com.example.Admin.dto.ExtendedRes;
import com.example.Admin.dto.MinimalRes;
import com.example.Admin.dto.OrderDto;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

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
