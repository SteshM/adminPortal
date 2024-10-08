package com.example.Admin.service.Components.Main;

import com.example.Admin.dto.AssignOrderToDepot;
import com.example.Admin.dto.ConfirmationDto;
import com.example.Admin.dto.MinimalRes;
import com.example.Admin.enums.OrderStatus;
import com.example.Admin.model.*;
import com.example.Admin.model.Analytics.OrderAnalyticalDetails;
import com.example.Admin.repository.*;
import com.example.Admin.service.Components.impl.AnalyticGuide;
import com.example.Admin.service.Components.utils.MyDtoMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class OrderService {
        private final OrderRepository orderRepository;
        private final DepotRepository depotRepository;
        private final OrderItemsRepository orderItemRepository;
        private final DepotProductsRepo depotProductsRepo;
        private final AnalyticGuide analyticGuide;

        public List<Order> getAllOrders() {
            return orderRepository.findAll();
        }

        public List<SpringDataJaxb.OrderDto> getUnassignedOrders() {
            return orderRepository.findAll().stream()
                    .filter(order -> order.getDepot() == null)
                    .map(this::mapToOrderDto)
                    .collect(Collectors.toList());
        }


        private SpringDataJaxb.OrderDto mapToOrderDto(Order order) {
            return MyDtoMapper.mapDtoToClass(order, SpringDataJaxb.OrderDto.class);

            // Map order items to order item IDs if  we need  detailed information of each order item
//            List<Long> orderItemIds = order.getOrderItems().stream()
//                    .map(orderItem -> orderItem.getOrderItemId())
//                    .collect(Collectors.toList());
//            orderDto.setOrderItemId(orderItemIds);

        }

        public Object getDepots(Long orderId) {
            log.info("order id: {}", orderId);
            Order order = orderRepository.findById(orderId).get();


            // retailerRepository.findById(order.getRetailerId()).get();

//       List <Depot>  depot =depotRepository.findAll().stream()
//                .sorted((depot1, depot2) -> Double.compare(
//                        calculateDistance(retailerLocation, depot1.getLocation()),
//                        calculateDistance(retailerLocation, depot2.getLocation())
//                ))
//                   .collect(Collectors.toList());
//        return Collections.singletonList(MyDtoMapper.mapDtoToClass(depot, DepotDto.class));
            log.info("order name: {}", order.getOrderName());
            Shop shop = order.getShop();

            if(shop == null){
                return MinimalRes.builder()
                        .status(400)
                        .message("shop not found")
                        .build();
            }

            List<Depot> depots = depotRepository.findAll();
            List<Depot> depotsWithProducts = new ArrayList<>();

            for(int i = 0; i<depots.size(); i ++){
                if(checkProductAvailability(depots.get(i), order)){
                    depotsWithProducts.add(depots.get(i));
                }
            }

            List<DepotDistance> depotDistances = new ArrayList<>();
            for (Depot depot: depotsWithProducts){
                double distance= calculateDistance(shop.getShopLocation(),depot.getLocation());
                DepotDistance depotDistance = new DepotDistance();
                depotDistance.setDistance(distance);
                depotDistance.setDepot(depot);
                depotDistances.add(depotDistance);
            }
            depotDistances.sort(Comparator.comparingDouble(DepotDistance::getDistance));

            return depotDistances;
        }


        public MinimalRes confirmDelivery(ConfirmationDto confirmation) {
            Order order = orderRepository.findById(confirmation.getOrderId()).get();
            if (order.getDeliveryCode().equals(confirmation.getConfirmationCode())) {
                order.setStatus(OrderStatus.COMPLETED);
                orderRepository.save(order);
                return MinimalRes.builder()
                        .status(200)
                        .message("Delivery completed successfully")
                        .build();
            } else {
                return MinimalRes.builder()
                        .status(400)
                        .message("Delivery not successful")
                        .build();

            }}

        private final OrderAnalyticalDetailsRepo orderAnalyticalDetailsRepo;

        public MinimalRes assignOrderToDepot(AssignOrderToDepot assign) {
            Order order = orderRepository.findById(assign.getOrderId()).get();
            if(order == null){
                return MinimalRes.builder()
                        .status(400)
                        .message("Depot not found")
                        .build();
            }else if(order.getDepot() != null){
                return MinimalRes.builder()
                        .status(403)
                        .message("Order already assigned")
                        .build();
            }
            Depot depot = depotRepository.findById(assign.getDepotId()).get();

            OrderAnalyticalDetails orderAnalyticalDetails = orderAnalyticalDetailsRepo.getAnalyticsDetails(assign.getOrderId());

            order.setDepot(depot);
            order.setStatus(OrderStatus.RECEIVED);

            analyticGuide.addPayment(depot.getDepotId(), orderAnalyticalDetails.getTotalAmount());
            analyticGuide.addProduct(depot.getDepotId(), orderAnalyticalDetails.getProducts());
            analyticGuide.addOrder(depot.getDepotId());
            orderRepository.save(order);


            //negating the depot inventory
            List<DepotProduct> depotProducts = new ArrayList<>();

            List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

            for(OrderItem orderItem: orderItems){
                DepotProduct depotProduct = depotProductsRepo.findByProductAndQuantityAtAndDepot(orderItem.getProduct(), orderItem.getQuantityAttribute(), depot);
                depotProduct.setQuantity(depotProduct.getQuantity()-orderItem.getQuantity());
                depotProducts.add(depotProduct);
            }
            depotProductsRepo.saveAll(depotProducts);

            analyticGuide.addOrder(depot.getDepotId());
            return MinimalRes.builder()
                    .status(200)
                    .message("Order assigned  to depot successfully")
                    .build();
        }


        //checks if product with attribute exists in the depot
        public boolean checkProductAvailability(Depot depot, Order order) {
            List<OrderItem> orderItems= orderItemRepository.findByOrder(order);
            for(OrderItem orderItem: orderItems){
                DepotProduct depotProduct = depotProductsRepo.findByProductAndQuantityAtAndDepot(orderItem.getProduct(), orderItem.getQuantityAttribute(), depot);
                if(depotProduct == null){
                    return false;
                }
            }
            return true;
        }

        public double calculateDistance(Location retailer, Location depot) {

            double lat1 = retailer.getLat();
            double lon1 = retailer.getLongitude();
            double lat2 = depot.getLat();
            double lon2 = depot.getLongitude();

            // Earth radius in kilometers
            final double R = 6371;

            // Convert latitude and longitude from degrees to radians
            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);

            // Haversine formula
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // Distance in kilometers
            return R * c;

        }

        @Data
       class DepotDistance{
            private double distance;
            private Depot depot;
        }

    }
