package com.example.service.Components.Main;

//import com.example.Admin.dto.*;
import com.example.dto.*;
import com.example.enums.OrderStatus;
//import com.example.Admin.repository.*;
//import com.example.Admin.service.Components.utils.*;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.Components.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final DepotRepository depotRepository;
    private final OrderRepository orderRepository;
    private final ProcRepo procRepo;
    private final ProductsRepository productsRepository;
    private final DepotProductsRepo depotProductsRepo;
    private final QuantityAttributeRepo quantityAttributeRepo;
    private final AdminSuperAdminRepo adminRepo;
    private final Exchanger exchanger;
    private final DepotAdminRepo depotAdminRepo;
    private final MyUserRepo myUserRepo;
    private final OrderItemsRepository orderItemsRepository;


    @Value("${email.sender}")
    public String emailSenderUrl;

    public ExtendedRes getAdminById(Long id) {
        AdminSuperAdmin admin = adminRepo.findAdminById(id);
        return  ExtendedRes.builder()
                .status(200)
                .body(admin)
                .message("Found")
                .build();
    }
    public ExtendedRes getAllAdmins() {

        List<MyUser> adminList = myUserRepo.findAll().stream().filter(admin-> StreamMapper.checkProfile(admin, "Admin")).toList();

        List<AdminSuperAdmin> distributors = new ArrayList<>();
        adminList.forEach(admin->{
            AdminSuperAdmin adminSuperAdmin = StreamMapper.getAdminSuperAdminFromUser(admin);
            adminSuperAdmin.setDepotsAssigned(depotAdminRepo.findByMyUser(admin).size());
            distributors.add(adminSuperAdmin);
        });

        return  ExtendedRes.builder()
                .status(200)
                .body(distributors)
                .message("A list of all admins")
                .build();
    }
    public ExtendedRes getAllSuperAdmins() {
        List<AdminSuperAdmin> supers = procRepo.getSuperAdmins();
        return  ExtendedRes.builder()
                .status(200)
                .body(supers)
                .message("A list of all admins")
                .build();
    }


    public ExtendedRes updateById(UserUpdateDto userUpdateDto) {
        Optional<MyUser> myUser = myUserRepo.findById(userUpdateDto.getUserId());
        if(!myUser.isPresent()){
            return ExtendedRes.builder()
                    .status(400)
                    .message("No user was found")
                    .build();
        }
        myUser.get().setFullName(userUpdateDto.getFullName());
        myUser.get().setContact(PhoneNumberEditor.resolveNumber(userUpdateDto.getContact()));
        if(myUser.get().getContact() == null){
            return ExtendedRes.builder()
                    .status(405)
                    .message("Bad contact format")
                    .build();
        }
        myUser.get().setEmployeeNo(userUpdateDto.getEmployeeNo());

        myUserRepo.save(myUser.get());
        UserResponseDto userResponseDto = MyDtoMapper.mapDtoToClass(myUser.get(), UserResponseDto.class);

        return ExtendedRes.builder()
                .status(200)
                .body(userResponseDto)
                .message("Admin updated successfully")
                .build();

    }

    public MinimalRes assignDepotAdmin(AssignDepotAdmin request) {
//to implement proper error handling
        Optional<MyUser> myUser = myUserRepo.findById(request.getAdminId());

        Depot depot = depotRepository.findById(request.getDepotId()).
                orElseThrow(() -> new RuntimeException("Depot with the given id not found found"));
        DepotAdmin depotAdmin = depotAdminRepo.findByMyUserAndDepot(myUser.get(), depot);
        if(depotAdmin != null){
            return MinimalRes.builder()
                    .status(400)
                    .success(false)
                    .message("Admin already assigned to the depot")
                    .build();
        }
        //saving depot admins relationship
        depotAdmin = new DepotAdmin();
        depotAdmin.setMyUser(myUser.get());
        depotAdmin.setDepot(depot);
        depotAdminRepo.save(depotAdmin);
        return MinimalRes.builder()
                .status(200)
                .success(true)
                .message("Admin assigned to depot successfully")
                .build();
    }

    public  MinimalRes dispatchOrder(DispatchOrder dispatch){
        Order order = orderRepository.findById(dispatch.getOrderId()).get();
        MyUser driver= myUserRepo.findById(dispatch.getDriverId()).get();
        String confirmationCode = RandomGenerator.generateRandom(5);
        order.setDeliveryCode(confirmationCode);
        MyUser retailer = order.getRetailer();

        //mail
        Map<String ,String> messageMap = new HashMap<>();
        messageMap.put("subject", "OTP(do not disclose)");
        messageMap.put("receiverName", retailer.getFullName());
        messageMap.put("templateName", "code");
        messageMap.put("to",retailer.getEmail());
        messageMap.put("deliveryCode", confirmationCode);
        messageMap.put("orderName", order.getOrderName());

        exchanger.sendPostEmailRequest(emailSenderUrl+"/mail/sendMail", messageMap);

        order.setDriver(driver);
        order.setStatus(OrderStatus.DISPATCHED);
        orderRepository.save(order);
        return  MinimalRes.builder()
                .status(200)
                .message("Order Dispatched successfully")
                .build();

    }


    public MinimalRes restock(RestockDto restockDto){
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        if(restockDto.getDepotId() == null){
            return MinimalRes.builder()
                    .status(205)
                    .message("Depot not selected")
                    .build();
        }
        Depot depot = depotRepository.findByDepotId(restockDto.getDepotId());
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
                    .message("Admin not assigned to depot")
                    .build();
        }
        Optional<Product> product = productsRepository.findById(restockDto.getProductId());
        if(!product.isPresent()){
            return MinimalRes.builder()
                    .status(400)
                    .message("product not found")
                    .build();
        }
        QuantityAttribute quantityAttribute = quantityAttributeRepo.findByProductAndQuantityAttId(product.get(), restockDto.getQAttId());
        if(quantityAttribute == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Attribute does not exist")
                    .build();
        }
        DepotProduct depotProduct = depotProductsRepo.findByProductAndQuantityAtAndDepot(product.get(), quantityAttribute,depot);
        if(depotProduct == null){
            depotProduct = new DepotProduct();
            depotProduct.setQuantityAt(quantityAttribute);
            depotProduct.setProduct(product.get());
            depotProduct.setDepot(depot);
            depotProduct.setQuantity(restockDto.getQuantity());
        }else{
            depotProduct.setQuantity(depotProduct.getQuantity()+restockDto.getQuantity());
        }
        depotProductsRepo.save(depotProduct);
        return MinimalRes.builder()
                .status(200)
                .message("Restocked successfully")
                .build();
    }

    public ExtendedRes getRetailersOrders(){
        List<Order> orders = orderRepository.findAll();
        List<RetailerOrder> retailerOrders = new ArrayList<>();

        orders.forEach(order->{
            RetailerOrder retailerOrder = StreamMapper.getRetailerOrderFromOrder(order);
            retailerOrder.setOrders(orderItemsRepository.countByOrder(order));
            retailerOrders.add(retailerOrder);
        });


        return ExtendedRes.builder()
                .status(200)
                .message("retailers and orders")
                .body(retailerOrders)
                .build();
    }

    public ExtendedRes getAdminDepots(Long adminId){
        Optional<MyUser> admin = myUserRepo.findById(adminId);
        if(admin.isPresent()){
            List<DepotAdmin> depotAdmins = depotAdminRepo.findByMyUser(admin.get());
            List<Depot> depots = depotAdmins.stream().map(AdminService::depotAdminToDepot).toList();
            return ExtendedRes.builder()
                    .status(200)
                    .message("Depot for admin "+admin.get().getFullName())
                    .body(depots)
                    .build();
        }else{
            return ExtendedRes.builder()
                    .status(400)
                    .message("Admin not found")
                    .build();
        }

    }

    private static Depot depotAdminToDepot(DepotAdmin depotAdmin){
        return depotAdmin.getDepot();
    }

    public ExtendedRes getMyDepots(){
        MyUser admin = myUserRepo.findByEmail(UserName.getUsername());
        if(admin != null){
            List<DepotAdmin> depotAdmins = depotAdminRepo.findByMyUser(admin);
            List<Depot> depots = depotAdmins.stream().map(AdminService::depotAdminToDepot).toList();
            return ExtendedRes.builder()
                    .status(200)
                    .message("Depot for admin "+admin.getFullName())
                    .body(depots)
                    .build();
        }else{
            return ExtendedRes.builder()
                    .status(400)
                    .message("Admin not found")
                    .build();
        }

    }

    public ExtendedRes getdriversWithNoTrucks(){
        return ExtendedRes.builder()
                .status(200)
                .message("Drivers with no trucks")
                .body(procRepo.getDriversWithNoTrucks())
                .build();
    }

}
