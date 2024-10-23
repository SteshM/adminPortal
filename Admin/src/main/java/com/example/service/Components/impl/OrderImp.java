package com.example.service.Components.impl;


//import com.example.Admin.dto.*;
import com.example.dto.*;
import com.example.enums.OrderStatus;
//import com.example.Admin.repository.*;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.Components.Main.OrderInterface;
//import com.example.Admin.service.Components.utils.*;
import com.example.service.Components.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderImp implements OrderInterface {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Autowired
    QuantityAttributeRepo quantityAttributeRepo;
    @Autowired
    ProcRepo procRepo;
    @Autowired
    CartItemRepo cartItmemRepo;
    @Autowired
    MyUserRepo myUserRepo;
    @Autowired
    ShopRepo shopRepo;
    @Autowired
    AdminSuperAdminRepo adminSuperAdminRepo;
    @Autowired
    ProfileRepo profileRepo;
    @Autowired
    DepotRepository depotRepository;
    @Autowired
    DepotAdminRepo depotAdminRepo;


    @Override
    public MinimalRes placeOrder(OrderDto orderDto) {
        MyUser retailer = myUserRepo.findByEmail(UserName.getUsername());
        Profile profile = profileRepo.findById(2l).get();
        if(!retailer.getProfiles().contains(profile)){
            retailer = null;
        }
        if(retailer == null){
            return MinimalRes.builder()
                    .status(500)
                    .message("only retailer can order")
                    .build();
        }
        Shop shop = shopRepo.findByShopIdAndMyUser(orderDto.getShopId(), retailer);
        if(shop == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Shop not found")
                    .build();
        }
        Order order = new Order();
        String orderName = RandomGenerator.generateRandom(5);
        order.setOrderName(orderName);
        order.setShop(shop);
        order.setRetailer(retailer);
        order.setStatus(OrderStatus.PENDING);
        Order completedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        List<Long> cartItemIds = new ArrayList<>();
        for(OrderItemDto orderItemDto: orderDto.getOrderItems()){
            OrderItem orderItem = new OrderItem();
            Product product = productsRepository.findByProductId(orderItemDto.getProductId());
            QuantityAttribute quantityAttribute = quantityAttributeRepo.findByProductAndQuantityAttId(product, orderItemDto.getQAttrId());
            if(quantityAttribute == null){
                return MinimalRes.builder()
                        .status(409)
                        .message("The attribute is invalid")
                        .build();
            }
            orderItem.setOrder(completedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setQuantityAttribute(quantityAttribute);
            cartItemIds.add(orderItemDto.getCartItemId());

            if(quantityAttribute.getOfferFrom() != null|| quantityAttribute.getOfferTo() != null){
                if(DateUtils.dayBeforeToday(quantityAttribute.getOfferFrom()) && DateUtils.dayAfterToday(quantityAttribute.getOfferTo())){
                    orderItem.setTotalAmount(quantityAttribute.getOfferPrice()*orderItem.getQuantity());
                }else{
                    orderItem.setTotalAmount(quantityAttribute.getPrice()*orderItem.getQuantity());
                }
            }else{
                orderItem.setTotalAmount(quantityAttribute.getPrice()*orderItem.getQuantity());
            }
            orderItems.add(orderItem);
        }
        cartItmemRepo.deleteAllById(cartItemIds);
        orderItemsRepository.saveAll(orderItems);
        return MinimalRes.builder()
                .status(200)
                .message("order placed")
                .build();
    }

    @Override
    public ExtendedRes getOrderItemDetails(Long orderId) {
        return ExtendedRes.builder()
                .status(200)
                .message("success")
                .body(procRepo.getOrderItemsDetailsByOrderId(orderId))
                .build();
    }

    @Override
    public MinimalRes addCartItem(CartItemRequest cartItemRequest) {
        MyUser retailer = myUserRepo.findByEmail(UserName.getUsername());
        Product product = productsRepository.findByProductId(cartItemRequest.getProductId());
        QuantityAttribute quantityAttribute = quantityAttributeRepo.findByProductAndQuantityAttId(product, cartItemRequest.getQuantityAtId());
        if(retailer == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Retailer not found")
                    .build();
        }
        if(quantityAttribute == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Attribute not found")
                    .build();
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setRetailer(retailer);
            cartItem.setQuantityAttribute(quantityAttribute);
            cartItmemRepo.save(cartItem);
            return MinimalRes.builder()
                    .status(200)
                    .message("Added to cart")
                    .build();
        }
    }

    @Override
    public ExtendedRes getCartItems() {
        MyUser retailer = myUserRepo.findByEmail(UserName.getUsername());
        List<CartItemDto> cartItemDtos = cartItmemRepo.findByRetailer(retailer).stream().map(StreamMapper::getCartItemDtoFromCartItem).toList();
        List<CartItemResDto> cartItemResDtos = cartItemDtos.stream().map(
                cartItemDto->{
                    CartItemResDto cartItemResDto = MyDtoMapper.mapDtoToClass(cartItemDto, CartItemResDto.class);

                    if(cartItemDto.getOfferFrom() != null|| cartItemDto.getOfferTo() != null){
                        if(DateUtils.dayBeforeToday(cartItemDto.getOfferFrom()) && DateUtils.dayAfterToday(cartItemDto.getOfferTo())){
                            cartItemResDto.setProductAttrPrice(cartItemDto.getOfferPrice());
                        }else{
                            cartItemResDto.setProductAttrPrice(cartItemDto.getProductAttrPrice());
                        }
                    }else{
                        cartItemResDto.setProductAttrPrice(cartItemDto.getProductAttrPrice());
                    }
                    return cartItemResDto;
                }
        ).toList();
        return ExtendedRes.builder()
                .status(200)
                .message("cart items response")
                .body(cartItemResDtos)
                .build();
    }

    @Override
    public MinimalRes deleteCartItem(Long cartItemId) {
        MyUser retailer = myUserRepo.findByEmail(UserName.getUsername());
        if(retailer == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Retailer not found")
                    .build();
        }
        cartItmemRepo.deleteByIdAndRetailerId(cartItemId, retailer.getId());
        return MinimalRes.builder()
                .status(200)
                .message("Deleted successfully")
                .build();
    }

    @Override
    public ExtendedRes getRetailerAndOrders(Long retailerId){
        MyUser retailer = myUserRepo.findById(retailerId).get();
        if(retailer == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("Retailer not found")
                    .build();
        }
        List<Order> orders = orderRepository.findByRetailer(retailer);
        return ExtendedRes.builder()
                .status(200)
                .message("Got retailer and his order")
                .body(orders)
                .build();
    }

    @Override
    public ExtendedRes retailerGetHisOrders(){
        MyUser retailer = myUserRepo.findByEmail(UserName.getUsername());
        if(retailer == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("Retailer not found")
                    .build();
        }else{
            List<Order> orders = orderRepository.findByRetailer(retailer);
            return ExtendedRes.builder()
                    .status(200)
                    .message("Got retailer and his order")
                    .body(orders)
                    .build();
        }
    }

    @Override
    public ExtendedRes getDepotOrders(Long depotId) {
        Depot depot = depotRepository.findByDepotId(depotId);
        List<Order> orders = orderRepository.findByDepot(depot);
        return ExtendedRes.builder()
                .status(200)
                .body(orders)
                .message("List of orders in a depot")
                .build();
    }

    //gets all the orders assigned to depots that the admin has
    @Override
    public ExtendedRes getMyDepotOrders() {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        List<DepotAdmin> depotAdmins = depotAdminRepo.findByMyUser(admin);
        List<Order> allOrders = new ArrayList<>();

        depotAdmins.forEach(depotAdmin->{
            List<Order> orders = orderRepository.findByDepot(depotAdmin.getDepot());
            allOrders.addAll(orders);
        });


        if(admin == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("No admin found")
                    .build();
        }

        List<RequestedOrderDto> requestedOrderDtos  = allOrders.stream().map(StreamMapper::getRequestedOrderDtofromOrder).toList();
        return ExtendedRes.builder()
                .status(200)
                .body(requestedOrderDtos)
                .message("List of orders in a depot")
                .build();

    }

    //gets all the orders that have been assigned to a driver in admin depots
    @Override
    public ExtendedRes getMyDepotAssignedOrders() {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        if(admin == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("No admin found")
                    .build();
        }
        List<DepotAdmin> depotAdmins = depotAdminRepo.findByMyUser(admin);
        List<Order> allOrders = new ArrayList<>();
        depotAdmins.forEach(depotAdmin->{
            List<Order> orders = orderRepository.findByDepotAndStatus(depotAdmin.getDepot(), OrderStatus.DISPATCHED);
            allOrders.addAll(orders);
        });

        List<RequestedAssignedOrderDto> requestedAssignedOrderDtos = allOrders.stream().map(StreamMapper::getRequestedAssignedOrderDtofromOrder).toList();
        return ExtendedRes.builder()
                .status(200)
                .body(requestedAssignedOrderDtos)
                .message("List of orders in a depot")
                .build();

    }


    //orders with no driver
    @Override
    public ExtendedRes getMyDepotUnAssignedOrders() {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        List<DepotAdmin> depotAdmins = depotAdminRepo.findByMyUser(admin);
        List<Order> allOrders =new ArrayList<>();
        depotAdmins.forEach(depotAdmin->{
            List<Order> orders = orderRepository.findByDepotAndStatus(depotAdmin.getDepot(), OrderStatus.RECEIVED);
            allOrders.addAll(orders);
        });

        List<RequestedOrderDto> requestedOrderDtos  = allOrders.stream().map(StreamMapper::getRequestedOrderDtofromOrder).toList();

        if(admin == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("No admin found")
                    .build();
        }
        return ExtendedRes.builder()
                .status(200)
                .body(requestedOrderDtos)
                .message("List of orders in a depot")
                .build();

    }

    @Override
    public ExtendedRes getMyCartItemsCount() {
        MyUser retailer = myUserRepo.findByEmail(UserName.getUsername());
        int count = cartItmemRepo.findByRetailer(retailer).size();
        return ExtendedRes.builder()
                .status(200)
                .message("cart size")
                .body(count)
                .build();
    }



}
