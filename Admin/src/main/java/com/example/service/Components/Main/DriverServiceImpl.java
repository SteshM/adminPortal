package com.example.service.Components.Main;

//import com.example.Admin.dto.*;
//import com.example.Admin.repository.*;
import com.example.dto.*;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.Components.utils.StreamMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final ProcRepo procRepo;
    private final DepotRepository depotRepository;
    private final TruckRepo truckRepo;
    private final ProfileRepo profileRepo;
    private final MyUserRepo myUserRepo;
    private final DepotDriverRepo depotDriverRepo;


    @Override
    public ExtendedRes findDriverById(Long driverId) {
        MyUser driver = myUserRepo.findById(driverId).get();

        Profile driverProfile = profileRepo.findById(4l).get();

        if(!driver.getProfiles().contains(driverProfile)){
            return  ExtendedRes.builder()
                    .status(400)
                    .message("Driver not found")
                    .build();
        }

        return  ExtendedRes.builder()
                .status(200)
                .body(driver)
                .message("Found")
                .build();
    }

    @Override
    public ExtendedRes findAllDrivers() {
        List<MyUser> myUsers = myUserRepo.findAll().stream().filter(myUser->
                StreamMapper.checkProfile(myUser, "Driver")&&!myUser.isSoftDelete()
        ).toList();


        List<DriverDto> drivers = new ArrayList<>();

        myUsers.forEach(user->{
            Truck truck = truckRepo.findByMyUser(user);
            DeportDriver depotDriver = depotDriverRepo.findByDriver(user);
            DriverDto driverDto = DriverDto.builder()
                    .contact(user.getContact())
                    .driverId(user.getId())
                    .email(user.getEmail())
                    .employeeNo(user.getEmployeeNo())
                    .fullName(user.getFullName())
                    .build();
            if(depotDriver != null){
                driverDto.setDepotId(depotDriver.getDepot().getDepotId());
                driverDto.setDepotName(depotDriver.getDepot().getName());
            }

            if(truck == null){
                driverDto.setTruckNo("No Truck");
            }else{
                driverDto.setTruckNo(truck.getTruckNo());
            }
            drivers.add(driverDto);
        });
        return  ExtendedRes.builder()
                .status(200)
                .message("A list of all drivers")
                .body(drivers)
                .build();
    }

    @Override
    public ExtendedRes updateById(DriverUpdateDto driverDto) {
        Optional<MyUser> myUser = myUserRepo.findById(driverDto.getDriverId());
        if(driverDto.getTruckId()!=null || driverDto.getTruckId() != 0){
            Optional<Truck> truck = truckRepo.findById(driverDto.getDriverId());
            if(truck.isEmpty()){
                return ExtendedRes.builder()
                        .status(400)
                        .message("The truck does not exist")
                        .build();
            }

            truck.get().setMyUser(myUser.get());
            truckRepo.save(truck.get());
        }
        myUser.get().setFullName(driverDto.getFullName());
        myUserRepo.save(myUser.get());
        return ExtendedRes.builder()
                .status(200)
                .message("Driver updated successfully")
                .build();
    }

    @Override
    public MinimalRes deleteById(Long id) {
        MyUser driver = myUserRepo.findById(id).get();
        driver.setSoftDelete(true);
        myUserRepo.save(driver);
        return MinimalRes.builder()
                .status(200)
                .message("Driver deleted successfully")
                .build();
    }
    // to implement this later

    public boolean confirmDelivery(String code) {
//        Order order = orderRepository.findByDeliveryCode(code);
//        if (order != null && "Pending".equals(order.getStatus())) {
//            order.setStatus(OrderStatus.valueOf("Received"));
//            orderRepository.save(order);
//            return true;
//        }
        return false;


    }

    @Override
    public ExtendedRes findAllDeletedDrivers() {
        List<DriverDto>  drivers= procRepo.getDriversBySoftDelete(true);
        return  ExtendedRes.builder()
                .status(200)
                .message("A list of all deleted drivers")
                .body(drivers)
                .build();
    }

    @Override
    public ExtendedRes getUnallocatedDrivers() {
        List<DriverDto>  drivers= procRepo.getUnallocatedDrivers(false);
        return  ExtendedRes.builder()
                .status(200)
                .message("A list of all unallocated drivers")
                .body(drivers)
                .build();
    }

    @Override
    public MinimalRes assignDriverToDepot(DriverAssignDto driverAssignDto) {
        MyUser driver = myUserRepo.findById(driverAssignDto.getDriverId()).get();

        Depot depot = depotRepository.findByDepotId(driverAssignDto.getDepotId());
        if(driver == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Driver not found")
                    .build();
        }
        if(depot == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Depot not found")
                    .build();
        }

        DeportDriver depotDriver = depotDriverRepo.findByDriver(driver);
        if(depotDriver == null){
            depotDriver = new DeportDriver();
            depotDriver.setDriver(driver);
        }
        depotDriver.setDepot(depot);
        depotDriverRepo.save(depotDriver);
        return MinimalRes.builder()
                .status(200)
                .message("Driver Assigned to depot")
                .build();
    }

}
