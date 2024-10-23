package com.example.service.Components.impl;

//import com.example.Admin.dto.*;
import com.example.dto.*;
import com.example.enums.OrderStatus;
//import com.example.Admin.repository.*;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.Components.Main.DepotOrderService;
import com.example.service.Components.utils.Exchanger;
import com.example.service.Components.utils.RandomGenerator;
import com.example.service.Components.utils.StreamMapper;
import com.example.service.Components.utils.UserName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DepotOrderServiceImpl implements DepotOrderService {
    private final DepotOrderRepo depotOrderRepo;
    private final DepotCartRepo depotCartRepo;
    private final DepotOrderItemRepo depotOrderItemRepo;
    private final DepotRepository depotRepository;
    private final ProductsRepository productsRepository;
    private final QuantityAttributeRepo quantityAttributeRepo;
    private final DepotAdminRepo depotAdminRepo;
    private final Exchanger exchanger;
    private final DepotProductsRepo depotProductsRepo;
    private final MyUserRepo myUserRepo;
    private final DepotOrderResponseRepo depotOrderResponseRepo;
    private final DepotDriverRepo depotDriverRepo;

    @Value("${email.sender}")
    String emailSenderUrl;

    @Override
    public MinimalRes addProductToCart(DepotCartDto depotOrderDto) {
        DepotCart depotCart = new DepotCart();
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());

        if(admin == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Admin not found")
                    .build();
        }
        Product product = productsRepository.findByProductId(depotOrderDto.getProductId());

        if(product == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Product not found")
                    .build();
        }

        QuantityAttribute quantityAttribute = quantityAttributeRepo.findByProductAndQuantityAttId(product, depotOrderDto.getQAttId());

        if(quantityAttribute == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Attribute not found")
                    .build();
        }
        depotCart.setAdmin(admin);
        depotCart.setProduct(product);
        depotCart.setQuantityAttribute(quantityAttribute);
        depotCart.setQuantity(depotOrderDto.getQuantity());

        depotCartRepo.save(depotCart);
        return MinimalRes.builder()
                .status(200)
                .message("Added to cart")
                .build();
    }

    @Override
    public MinimalRes makeOrder(DepotOrderRequestDto depotOrderRequestDto) {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());

        if(admin == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Admin not found")
                    .build();
        }
        Long depotId = depotOrderRequestDto.getDepotId();
        if(depotId == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("No depot selected")
                    .build();
        }

        Depot depot = depotRepository.findByDepotId(depotId);
        if(depot == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Depot not found")
                    .build();
        }

        DepotAdmin depotAdmin = depotAdminRepo.findByMyUserAndDepot(admin, depot);

        if(depotAdmin == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Admin not assigned to this depot")
                    .build();
        }

        DepotOrder depotOrder= new DepotOrder();


        depotOrder.setOrderName(RandomGenerator.generateRandom(7));
        depotOrder.setDepot(depot);
        depotOrder.setStatus(OrderStatus.PENDING);
        depotOrder = depotOrderRepo.save(depotOrder);


        List<DepotOrderItem> depotOrderItems = new ArrayList<>();
        List<DepotCart> depotCarts = new ArrayList<>();

        for(DepotOrderDto depotOrderDto: depotOrderRequestDto.getDepotOrderDtos()){
            Optional<DepotCart> depotCart = depotCartRepo.findById(depotOrderDto.getDepotCartId());
            if(!depotCart.isPresent()){
                return MinimalRes.builder()
                        .status(400)
                        .message("Cart item not found")
                        .build();
            }
            depotCarts.add(depotCart.get());

            Product product = depotCart.get().getProduct();

            if(product == null){
                return MinimalRes.builder()
                        .status(400)
                        .message("Product not found")
                        .build();
            }

            QuantityAttribute quantityAttribute = depotCart.get().getQuantityAttribute();

            if(quantityAttribute == null){
                return MinimalRes.builder()
                        .status(400)
                        .message("Attribute not found")
                        .build();
            }

            DepotOrderItem depotOrderItem = new DepotOrderItem();
            depotOrderItem.setDepotOrder(depotOrder);
            depotOrderItem.setProduct(product);
            depotOrderItem.setQuantityAttribute(quantityAttribute);
            depotOrderItem.setQuantity(depotOrderDto.getQuantity());
            depotOrderItems.add(depotOrderItem);
        }
        depotCartRepo.deleteAll(depotCarts);
        depotOrderItemRepo.saveAll(depotOrderItems);
        return MinimalRes.builder()
                .status(200)
                .message("Order made successfully")
                .build();
    }

    @Override
    public ExtendedRes getCartItems() {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        if(admin == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("Admin not found")
                    .build();
        }
        List<DepotCart> depotCarts = depotCartRepo.findByAdmin(admin);
        List<DepotCartResponseDto> depotCartResponses = depotCarts.stream().map(StreamMapper::getDepotCartResponseDtofromDepotCart).toList();
        return ExtendedRes.builder()
                .status(200)
                .message("Depot cart items")
                .body(depotCartResponses)
                .build();
    }


    ///used to get the restosk orders that has been assigned to the depots of the admin

    @Override
    public ExtendedRes getDepotOrders() {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        List<DepotOrderResponseDto> depotOrderResponseDtos= depotOrderResponseRepo.findAllAdminRestockOrders(admin.getId());
        return ExtendedRes.builder()
                .status(200)
                .message("Depot requested orders")
                .body(depotOrderResponseDtos)
                .build();
    }

    @Override
    public MinimalRes deleteCartItem(Long cartItemId) {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());

        DepotCart depotCart = depotCartRepo.findByCartIdAndAdmin(cartItemId, admin);
        if(depotCart == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Cart item not found")
                    .build();
        }

        depotCartRepo.deleteById(cartItemId);
        return MinimalRes.builder()
                .status(200)
                .message("Cart Item removed")
                .build();
    }

    @Override
    public ExtendedRes getOrderItems(Long depotOrderId){
        DepotOrder depotOrder= depotOrderRepo.findById(depotOrderId).get();

        if(depotOrder == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("Order not found")
                    .build();
        }
        List<DepotOrderItem> depotOrderItems = depotOrderItemRepo.findByDepotOrder(depotOrder);
        List<OrderItemDetails> orderItemDetails = depotOrderItems.stream().map(DepotOrderServiceImpl::getOrderItemDetails).toList();
        return ExtendedRes.builder()
                .status(200)
                .message("Items in this order")
                .body(orderItemDetails)
                .build();
    }

    private static OrderItemDetails getOrderItemDetails(DepotOrderItem depotOrderItem){
        return OrderItemDetails.builder()
                .productId(depotOrderItem.getProduct().getProductId())
                .productName(depotOrderItem.getProduct().getProductName())
                .productQattName(depotOrderItem.getQuantityAttribute().getAttrDescription())
                .productQatid(depotOrderItem.getQuantityAttribute().getQuantityAttId())
                .productImage(depotOrderItem.getQuantityAttribute().getPicture())
                .cloud(depotOrderItem.getQuantityAttribute().isCloud())
                .productDescription(depotOrderItem.getProduct().getDescription())
                .quantity(depotOrderItem.getQuantity())
                .build();
    }

    @Override
    public MinimalRes assignDepotOrderToDepot(AssigneDepotOrderToDepot assigneDepotOrderToDepot) {
        Depot depot = depotRepository.findByDepotId(assigneDepotOrderToDepot.getDepotId());
        Optional<DepotOrder> depotOrder= depotOrderRepo.findById(assigneDepotOrderToDepot.getDepotOrderId());

        if(!depotOrder.isPresent() || depot == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Some null values")
                    .build();
        }
        if(depotOrder.get().getDepot().equals(depot)){
            return MinimalRes.builder()
                    .status(400)
                    .message("Can't assign to the same depot")
                    .build();
        }

        if(depotOrder.get().getAssignedDepot() != null){
            return MinimalRes.builder()
                    .status(403)
                    .message("Already assigned order to depot")
                    .build();
        }

        depotOrder.get().setAssignedDepot(depot);
        depotOrder.get().setStatus(OrderStatus.RECEIVED);
        depotOrderRepo.save(depotOrder.get());

        //reduce the stocks

        return MinimalRes.builder()
                .status(200)
                .message("Assigned order to depot")
                .build();
    }

    @Override
    public ExtendedRes getInterdepotDepotOrders() {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        List<DepotOrderResponseDto> depotOrderResponseDtos= depotOrderResponseRepo.findAllAdminAssignedRestockOrders(admin.getId());
        return ExtendedRes.builder()
                .status(200)
                .message("Orders")
                .body(depotOrderResponseDtos)
                .build();
    }

    @Override
    public MinimalRes assignDepotOrderToDriver(AssignDepotOrderToDriver assignDepotOrderToDriver) {
        Optional<DepotOrder> depotOrder = depotOrderRepo.findById(assignDepotOrderToDriver.getDepotOrderId());
        if(!depotOrder.isPresent()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Order not found")
                    .build();
        }
        Long depotId = assignDepotOrderToDriver.getDepotId();
        Optional<MyUser> driver = myUserRepo.findById(assignDepotOrderToDriver.getDriverId());
        Depot depot = depotRepository.findByDepotId(depotId);
        DeportDriver depotDriver = depotDriverRepo.findByDriverAndDepot(driver.get(), depot);
        if(depotDriver == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Driver not found")
                    .build();
        }

        String deliveryCode = RandomGenerator.generateRandom(4);
        depotOrder.get().setMyUser(driver.get());
        depotOrder.get().setDeliveryCode(deliveryCode);
        depotOrder.get().setStatus(OrderStatus.DISPATCHED);
        depotOrderRepo.save(depotOrder.get());

        List<DepotAdmin> depotAdmins = depotAdminRepo.findByDepot(depotOrder.get().getDepot());

        //sending delivery code to all the admins in a depot
        for(DepotAdmin depotAdmin: depotAdmins){
            Map<String ,String> messageMap = new HashMap<>();
            messageMap.put("subject", "OTP(do not disclose)");
            messageMap.put("orderName", depotOrder.get().getOrderName());
            messageMap.put("templateName", "code");
            messageMap.put("receiverName", depotAdmin.getMyUser().getFullName());
            messageMap.put("to",depotAdmin.getMyUser().getEmail());
            messageMap.put("deliveryCode", deliveryCode);
            exchanger.sendPostEmailRequest(emailSenderUrl+"/mail/sendMail", messageMap);
        }


        return MinimalRes.builder()
                .message("Assigned depot to driver: "+driver.get().getFullName())
                .build();
    }

    @Override
    public ExtendedRes getDepotOrders( OrderStatus orderStatus) {
        List<DepotOrder> depotOrders = depotOrderRepo.findByStatus(orderStatus);
        List<DepotOrderRequest> depotOrderRequestDtos = depotOrders.stream().map(DepotOrderServiceImpl::getDepotOrderRequest).toList();
        return ExtendedRes.builder()
                .status(200)
                .message("Depot orders")
                .body(depotOrderRequestDtos)
                .build();
    }

    private static DepotOrderRequest getDepotOrderRequest(DepotOrder depotOrder){
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

    @Override
    public MinimalRes confirmDelivery(DepotOrderConfirmDelivery delivery) {

        Optional<DepotOrder> depotOrder = depotOrderRepo.findById(delivery.getDepotOrderId());
        if(!depotOrder.isPresent()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Depot order not found")
                    .build();
        }

        if(depotOrder.get().getStatus().equals(OrderStatus.COMPLETED)){
            return MinimalRes.builder()
                    .status(400)
                    .message("Order already confirmed")
                    .build();
        }

        if(!depotOrder.get().getDeliveryCode().equals(delivery.getDeliveryCode())){
            return MinimalRes.builder()
                    .status(400)
                    .message("Unknown delivery code")
                    .build();
        }

        List<DepotOrderItem> depotOrderItems = depotOrderItemRepo.findByDepotOrder(depotOrder.get());
        List<DepotProduct> depotProducts = new ArrayList<>();
        for(DepotOrderItem depotOrderItem: depotOrderItems){
            if(productsRepository.findById(depotOrderItem.getProduct().getProductId()) == null){
                return MinimalRes.builder()
                        .status(400)
                        .message("product not found")
                        .build();
            }
            DepotProduct depotProduct = depotProductsRepo.findByProductAndQuantityAtAndDepot(depotOrderItem.getProduct(), depotOrderItem.getQuantityAttribute(),depotOrder.get().getDepot());
            if(depotProduct == null){
                QuantityAttribute quantityAttribute = quantityAttributeRepo.findByProductAndQuantityAttId(depotOrderItem.getProduct(), depotOrderItem.getQuantityAttribute().getQuantityAttId());
                if(quantityAttribute == null){
                    return MinimalRes.builder()
                            .status(400)
                            .message("Attribute does not exist")
                            .build();
                }
                depotProduct = new DepotProduct();
                depotProduct.setQuantityAt(quantityAttribute);
                depotProduct.setProduct(depotOrderItem.getProduct());
                depotProduct.setDepot(depotOrder.get().getDepot());
                depotProduct.setQuantity(depotOrderItem.getQuantity());
            }else{
                depotProduct.setQuantity(depotProduct.getQuantity()+depotOrderItem.getQuantity());
            }
            depotProducts.add(depotProduct);
        }
        depotOrder.get().setStatus(OrderStatus.COMPLETED);
        depotProductsRepo.saveAll(depotProducts);
        depotOrderRepo.save(depotOrder.get());
        return MinimalRes.builder()
                .status(200)
                .message("Restocked successfully")
                .build();
    }

    @Override
    public ExtendedRes getMyDepotOrders() {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        List<DepotOrderResponseDto> depotOrderResponseDtos= depotOrderResponseRepo.findAllAdminRestockOrders(admin.getId());
        return ExtendedRes.builder()
                .status(200)
                .message("Depot requested orders")
                .body(depotOrderResponseDtos)
                .build();
    }

// private static DepotOrderResponseDto getDepotOrderResponseDto(DepotOrder depotOrder){
//     DepotOrderResponseDto depotOrderResponseDto = DepotOrderResponseDto.builder()
//     .depotTo(depotOrder.getDepot().getName())
//     .depotOrderId(depotOrder.getId())
//     .orderName(depotOrder.getOrderName())
//     .orderDate(depotOrder.getOrderDate())
//     .orderStatus(depotOrder.getStatus())
//     .build();

//     if(depotOrder.getMyUser() != null){
//         depotOrderResponseDto.setDriverId(depotOrder.getMyUser().getId());
//         depotOrderResponseDto.setDriverName(depotOrder.getMyUser().getFullName());
//         Truck truck = truckRepo.findByMyUser(depotOrder.getMyUser());
//         depotOrderResponseDto.setTruckNo(truck.getTruckNo());
//     }

//     if(depotOrder.getAssignedDepot() != null){
//         depotOrderResponseDto.setDepotFrom(depotOrder.getAssignedDepot().getName());
//     }
//     return depotOrderResponseDto;
// }

    @Override
    public MinimalRes updateCartItemQuanity(DepotCartQuantityDto depotCartQuantityDto) {
        Optional<DepotCart> depotCart = depotCartRepo.findById(depotCartQuantityDto.getDepotCartId());
        if(!depotCart.isPresent()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Cart item not found")
                    .build();
        }
        depotCart.get().setQuantity(depotCartQuantityDto.getQuantity());
        depotCartRepo.save(depotCart.get());
        return MinimalRes.builder()
                .status(200)
                .message("Quantity updated")
                .build();
    }

}
