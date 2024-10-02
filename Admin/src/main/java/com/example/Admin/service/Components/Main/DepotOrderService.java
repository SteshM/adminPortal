package com.example.Admin.service.Components.Main;

import com.example.Admin.dto.*;
import com.example.Admin.enums.OrderStatus;

public interface DepotOrderService {
    public MinimalRes addProductToCart(DepotCartDto depotCartDto);
    public MinimalRes makeOrder(DepotOrderRequestDto depotOrderRequestDto);
    public ExtendedRes getCartItems();
    public ExtendedRes getDepotOrders();
    public ExtendedRes getDepotOrders( OrderStatus orderStatus);
    public ExtendedRes getOrderItems(Long depotOrderId);
    public MinimalRes deleteCartItem(Long cartItemId);
    public MinimalRes assignDepotOrderToDepot(AssigneDepotOrderToDepot assigneDepotOrderToDepot);
    public ExtendedRes getInterdepotDepotOrders();
    public MinimalRes assignDepotOrderToDriver(AssignDepotOrderToDriver assignDepotOrderToDriver);
    public MinimalRes confirmDelivery(DepotOrderConfirmDelivery delivery);
    public ExtendedRes getMyDepotOrders();
    public MinimalRes updateCartItemQuanity(DepotCartQuantityDto depotCartQuantityDto);


}
