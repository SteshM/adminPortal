package com.example.service.Components.impl;

import com.example.dto.DepotOrderRequest;
import com.example.dto.DriverDto;
import com.example.dto.ExtendedRes;
import com.example.dto.RequestedOrderDto;
import com.example.model.DepotOrder;
import com.example.model.MyUser;
import com.example.model.Order;
import com.example.model.Truck;
//import com.example.Admin.repository.*;
import com.example.repository.*;
import com.example.service.Components.utils.StreamMapper;
import com.example.service.Components.utils.UserName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverImpl implements DriverGuide {

    @Autowired
    DriverDtoRepo driverDtoRepo;
    @Autowired
    ProcRepo procRepo;
    @Autowired
    DepotOrderRepo depotOrderRepo;
    @Autowired
    MyUserRepo myUserRepo;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    TruckRepo truckRepo;

    @Override
    public ExtendedRes getMyOrders() {
        MyUser driver = myUserRepo.findByEmail(UserName.getUsername());
        List<Order> orders = orderRepository.findByDriver(driver);
        List<RequestedOrderDto> requestedOrderDtos = orders.stream().map(StreamMapper::getRequestedOrderDtofromOrder).toList();

        return ExtendedRes.builder()
                .status(200)
                .message("List of orders")
                .body(requestedOrderDtos)
                .build();
    }

    @Override
    public ExtendedRes getMyDetails() {
        DriverDto driver = driverDtoRepo.getDriverWithEmailAndSoftDelete(UserName.getUsername(), false);
        if(driver == null){
            return ExtendedRes.builder()
                    .status(400)
                    .message("driver not found")
                    .build();
        }else{

            return ExtendedRes.builder()
                    .status(200)
                    .message("driver details")
                    .body(driver)
                    .build();
        }
    }

    @Override
    public ExtendedRes myTruck() {
        MyUser driver = myUserRepo.findByEmail(UserName.getUsername());
        Truck truck = truckRepo.findByMyUser(driver);
        return ExtendedRes.builder()
                .status(200)
                .message("My Truck details")
                .body(truck)
                .build();
    }

    @Override
    public ExtendedRes getMyInternalOrders() {
        MyUser driver = myUserRepo.findByEmail(UserName.getUsername());
        List<DepotOrder> depotOrders= depotOrderRepo.findByMyUser(driver);
        List<DepotOrderRequest> depotOrderRequests = depotOrders.stream().map(
                StreamMapper::getDepotOrderRequest
        ).toList();
        return ExtendedRes.builder()
                .status(200)
                .message("Internal orders")
                .body(depotOrderRequests)
                .build();
    }




}
