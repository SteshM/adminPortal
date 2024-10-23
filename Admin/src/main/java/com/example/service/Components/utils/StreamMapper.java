package com.example.service.Components.utils;

//import com.example.Admin.dto.*;
import com.example.dto.*;
import com.example.model.*;

public class StreamMapper {

        public static StockDto getStockDtoFromDepotProduct(DepotProduct depotProduct){
            return StockDto.builder()
                    .cloud(depotProduct.getQuantityAt().isCloud())
                    .picture(depotProduct.getQuantityAt().getPicture())
                    .productAttributeId(depotProduct.getQuantityAt().getQuantityAttId())
                    .productAttributeName(depotProduct.getQuantityAt().getAttrDescription())
                    .productId(depotProduct.getProduct().getProductId())
                    .productName(depotProduct.getProduct().getProductName())
                    .quantity(depotProduct.getQuantity())
                    .build();
        }

        public static RequestedOrderDto getRequestedOrderDtofromOrder(Order order){
            return RequestedOrderDto.builder()
                    .contact(order.getRetailer().getContact())
                    .depoId(order.getDepot().getDepotId())
                    .depotName(order.getDepot().getName())
                    .location(order.getShop().getShopLocation())
                    .orderId(order.getOrderId())
                    .orderName(order.getOrderName())
                    .retailerName(order.getRetailer().getFullName())
                    .shopName(order.getShop().getShopName())
                    .status(order.getStatus())
                    .build();
        }
        public static DepotOrderRequest getDepotOrderRequest(DepotOrder depotOrder){
            DepotOrderRequest depotOrderRequest =  DepotOrderRequest.builder()
                    .depoId(depotOrder.getDepot().getDepotId())
                    .depotOrderId(depotOrder.getId())
                    .depotToName(depotOrder.getDepot().getName())
                    .orderName(depotOrder.getOrderName())
                    .status(depotOrder.getStatus())
                    .location(depotOrder.getDepot().getLocation())
                    .orderDate(depotOrder.getOrderDate())
                    .build();

            if(depotOrder.getAssignedDepot() != null){
                depotOrderRequest.setDepotFromName(depotOrder.getAssignedDepot().getName());
            }
            return depotOrderRequest;
        }

        public static CartItemDto getCartItemDtoFromCartItem(CartItem cartItem){
            return CartItemDto.builder()
                    .cartItemId(cartItem.getCartItemId())
                    .cloud(cartItem.getQuantityAttribute().isCloud())
                    .offerFrom(cartItem.getQuantityAttribute().getOfferFrom())
                    .offerTo(cartItem.getQuantityAttribute().getOfferTo())
                    .offerPrice(cartItem.getQuantityAttribute().getOfferPrice())
                    .productAttrId(cartItem.getQuantityAttribute().getQuantityAttId())
                    .picture(cartItem.getQuantityAttribute().getPicture())
                    .productAttrName(cartItem.getQuantityAttribute().getAttrDescription())
                    .productAttrPrice(cartItem.getQuantityAttribute().getPrice())
                    .quantity(cartItem.getQuantity())
                    .productId(cartItem.getProduct().getProductId())
                    .productName(cartItem.getProduct().getProductName())
                    .build();
        }

        public static RequestedAssignedOrderDto getRequestedAssignedOrderDtofromOrder(Order order){
            return RequestedAssignedOrderDto.builder()
                    .contact(order.getRetailer().getContact())
                    .depoId(order.getDepot().getDepotId())
                    .depotName(order.getDepot().getName())
                    .location(order.getShop().getShopLocation())
                    .orderId(order.getOrderId())
                    .orderName(order.getOrderName())
                    .retailerName(order.getRetailer().getFullName())
                    .shopName(order.getShop().getShopName())
                    .driverContact(order.getDriver().getContact())
                    .driverEmail(order.getDriver().getEmail())
                    .driverName(order.getDriver().getFullName())
                    .driverId(order.getDriver().getId())
                    .status(order.getStatus())
                    .build();
        }

        public static AdminSuperAdmin getAdminSuperAdminFromUser(MyUser admin){
            return AdminSuperAdmin.builder()
                    .adminId(admin.getId())
                    .adminName(admin.getFullName())
                    .cloud(admin.isCloud())
                    .profilePic(admin.getPicture())
                    .contact(admin.getContact())
                    .employeeNo(admin.getEmployeeNo())
                    .build();
        }


        public static boolean checkProfile(MyUser user, String profileName){
            for(Profile profile: user.getProfiles()){
                if(profile.getProfileName().equals(profileName)){
                    return true;
                }
            }
            return false;
        }

        public static RetailerOrder getRetailerOrderFromOrder(Order order){
            RetailerOrder retailerOrder =  RetailerOrder.builder()
                    .orderDate(order.getOrderDate())
                    .orderId(order.getOrderId())
                    .orderName(order.getOrderName())
                    .retailerId(order.getRetailer().getId())
                    .retailerName(order.getRetailer().getFullName())
                    .shopName(order.getShop().getShopName())
                    .status(order.getStatus())
                    .build();

            if(order.getDepot() != null){
                retailerOrder.setDepotName(order.getDepot().getName());
            }

            if(order.getDriver() != null){
                retailerOrder.setDriverEmail(order.getDriver().getEmail());
                retailerOrder.setDriverName(order.getDriver().getFullName());
            }
            return retailerOrder;
        }

        public static DepotCartResponseDto getDepotCartResponseDtofromDepotCart(DepotCart depotCart){
            return DepotCartResponseDto.builder()
                    .attrName(depotCart.getQuantityAttribute().getAttrDescription())
                    .cartItemId(depotCart.getCartId())
                    .productId(depotCart.getProduct().getProductId())
                    .productName(depotCart.getProduct().getProductName())
                    .qAttId(depotCart.getQuantityAttribute().getQuantityAttId())
                    .quantity(depotCart.getQuantity())
                    .build();
        }
}
