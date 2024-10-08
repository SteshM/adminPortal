package com.example.Admin.service.Components.impl;

import com.example.Admin.dto.*;
import com.example.Admin.exception.ResourceNotFoundException;
import com.example.Admin.model.*;
import com.example.Admin.repository.*;
import com.example.Admin.service.Components.Main.DepotService;
import com.example.Admin.service.Components.Main.OrderService;
import com.example.Admin.service.Components.utils.MyDtoMapper;
import com.example.Admin.service.Components.utils.StreamMapper;
import com.example.Admin.service.Components.utils.UserName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepotServiceImpl implements DepotService {
    private final DepotRepository depotRepository;
    private final LocationRepository locationRepository;
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProcRepo procRepo;
    private final MyUserRepo myUserRepo;
    private final DepotProductsRepo depotProductsRepo;

    @Override
    public MinimalRes createDepot(DepotDto depotDto) {
        Depot depot = MyDtoMapper.mapDtoToClass(depotDto, Depot.class);
        depot.setCloud(false);

        locationRepository.save(depot.getLocation());
        depotRepository.save(depot);
        return MinimalRes.builder()
                .status(200)
                .message("Depot created successfully")
                .build();
    }

    //to implement proper error handling
    @Override
    public ExtendedRes findDepotById(Long id) {
        Depot depot = depotRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Depot", "id", id));


        DepotDto depotDto = MyDtoMapper.mapDtoToClass(depot, DepotDto.class);
        return ExtendedRes.builder()
                .status(200)
                .message("Success")
                .body(depotDto)
                .build();
    }

    @Override
    public ExtendedRes findAllDepots() {
        List<Depot> depots = depotRepository.findAll();
        List<DepotDto> depotDtos = depots.stream()
                .map(depot -> MyDtoMapper.mapDtoToClass(depot, DepotDto.class))
                .collect(Collectors.toList());
        return ExtendedRes.builder()
                .status(200)
                .body(depotDtos)
                .message("A list of all depots")
                .build();

    }

    @Override
    public ExtendedRes updateById(DepotDto depotDto) {
        Depot existingDepot = depotRepository.findById(depotDto.getDepotId()).orElseThrow(
                () -> new ResourceNotFoundException("Depot", "id", depotDto.getDepotId()));
        existingDepot.setName(depotDto.getName());
        Location existingLocation = locationRepository.findById(existingDepot.getLocation().getId()).get();

        Location location = depotDto.getLocation();
        location.setId(existingLocation.getId());

        existingDepot.setLocation(locationRepository.save(location));

        Depot updatedDepot = depotRepository.save(existingDepot);
        DepotDto depotDto1 = MyDtoMapper.mapDtoToClass(updatedDepot, DepotDto.class);
        return ExtendedRes.builder()
                .status(200)
                .message("Depot updated")
                .body(depotDto1)
                .build();

    }

    @Override
    public MinimalRes deleteById(Long id) {
        Depot existingDepot = depotRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id));

        depotRepository.delete(existingDepot);
        return MinimalRes.builder()
                .status(200)
                .message("Depot deleted successfully")
                .build();

    }
    public List<DepotDistance> getDepotsWithDistances(Long orderId) {

        Order order = orderRepository.findById(orderId).get();
        Shop shop = order.getShop();
        Location retailerLocation = shop.getShopLocation();
        List<OrderItem> orderItems = orderItemsRepository.findByOrder(order);
        List<Long> productIds = orderItems.stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toList());

//        List<Integer> quantities = orderItems.stream()
//                .map(OrderItem::getQuantity)
//                .collect(Collectors.toList());
        int totalQuantity = orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        long productCount = productIds.size();

        List<Long> depotIds = depotRepository.findDepotIdsWithProducts(productIds, totalQuantity, productCount);

        return depotIds.stream()
                .map(depotId -> {
                    Depot depot = depotRepository.findByDepotId(depotId);
                    Location depotLocation = depot.getLocation();


                    double distance = calculateDistance(retailerLocation.getLat(), retailerLocation.getLongitude(), depotLocation.getLat(), depotLocation.getLongitude());
                    return new DepotDistance(depotId,depot.getName(), distance);
                })
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    @Override
    public ExtendedRes getMyStockDetails() {
        MyUser myUser = myUserRepo.findByEmail(UserName.getUsername());
        if(myUser == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("User not found")
                    .build();
        }

        return ExtendedRes.builder()
                .status(200)
                .message("Depot stock")
                .body(procRepo.getDepotStock(myUser.getId()))
                .build();
    }
    @Override
    public ExtendedRes getDepotStockDetails(Long depotId) {
        Depot depot = depotRepository.findByDepotId(depotId);
        List<DepotProduct> depotProducts = depotProductsRepo.findByDepot(depot);
        List<StockDto> stockDtos = depotProducts.stream().map(StreamMapper::getStockDtoFromDepotProduct).toList();
        return ExtendedRes.builder()
                .status(200)
                .message("Depot stock")
                .body(stockDtos)
                .build();
    }
    @Override
    public ExtendedRes myDepotDrivers() {
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());

        if(admin == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("User not found")
                    .build();
        }
        return ExtendedRes.builder()
                .status(200)
                .message("Depot drivers")
                .body(procRepo.getDepotDrivers(admin.getId()))
                .build();
    }
    @Override
    public ExtendedRes getDepotDrivers(Long depotId) {
        return ExtendedRes.builder()
                .status(200)
                .message("Depot drivers")
                .body(procRepo.getDepotDrivers(depotId))
                .build();
    }
}