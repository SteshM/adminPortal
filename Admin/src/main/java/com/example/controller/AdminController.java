package com.example.controller;

//import com.example.Admin.dto.*;
import com.example.dto.*;
import com.example.service.Components.Main.AdminService;
import com.example.service.Components.Main.DepotOrderService;
import com.example.service.Components.Main.DepotService;
import com.example.service.Components.Main.OrderInterface;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/admin/")
public class AdminController {
    private final AdminService adminService;
    private final OrderInterface orderInterface;
    private final DepotService depotService;
    private final DepotOrderService depotOrderService;


    @PostMapping("dispatchOrder")
    public MinimalRes dispatchOrder(@RequestBody DispatchOrder dispatchOrder){
        return adminService.dispatchOrder(dispatchOrder);
    }

    @GetMapping("admin/getAssignedDepots")
    public ExtendedRes getAssignedDepots(){
        return  adminService.getMyDepots();

    }
    @PostMapping("restock")
    public  MinimalRes restock(@RequestBody RestockDto restockDto){
        return adminService.restock(restockDto);
    }

    @GetMapping("getOrdersInMyDepot")
    public ExtendedRes getOrdersInMyDeopt(){
        return orderInterface.getMyDepotOrders();
    }
    @GetMapping("getAssignedOrdersInMyDeopt")
    public ExtendedRes getAssignedOrdersInMyDeopt(){
        return orderInterface.getMyDepotAssignedOrders();
    }
    @GetMapping("getUnAssignedOrdersInMyDeopt")
    public ExtendedRes getUnAssignedOrdersInMyDeopt(){
        return orderInterface.getMyDepotUnAssignedOrders();
    }

    @GetMapping("myDepotDrivers")
    public ExtendedRes myDepotDrivers(){
        return depotService.myDepotDrivers();
    }

    @GetMapping("myStock")
    public ExtendedRes getMyStock(){
        return depotService.getMyStockDetails();
    }
    @PostMapping("addToCart")
    public MinimalRes addProductToCart(@RequestBody DepotCartDto depotOrderDto) {
        return depotOrderService.addProductToCart(depotOrderDto);
    }
    @PostMapping("makeOrder")
    public MinimalRes makeOrder(@RequestBody DepotOrderRequestDto depotOrderDtos) {
        return depotOrderService.makeOrder(depotOrderDtos);
    }
    @GetMapping("getCartItems")
    public ExtendedRes getCartItems() {
        return depotOrderService.getCartItems();
    }

//    @Operation(
//            description = "These are the orders that have been assigned to the selected depot"
//    )
    @GetMapping("getDepotOrders")
    public ExtendedRes getDepotOrders() {
        return depotOrderService.getDepotOrders();
    }
//
//    @Operation(
//            description = "These are the orders that the selected Id has made"
//    )
    @GetMapping("getMyDepotOrders")
    public ExtendedRes getMyDepotOrders() {
        return depotOrderService.getMyDepotOrders();
    }

    @GetMapping("getOrderItems/{depotOrderId}")
    public ExtendedRes getOrderItems(@PathVariable Long depotOrderId) {
        return depotOrderService.getOrderItems(depotOrderId);
    }

    @GetMapping("delete/cartItem/{cartItemId}")
    public MinimalRes deleteCartItem(@PathVariable Long cartItemId) {
        return depotOrderService.deleteCartItem(cartItemId);
    }

    @GetMapping("getAssignedDepotOrders")
    public ExtendedRes getAssignedDepotOrders(){
        return depotOrderService.getInterdepotDepotOrders();
    }
    @PostMapping("/assign/AssignDepotOrderToDriver")
    public  MinimalRes assignDepotOrderToDriver(@RequestBody AssignDepotOrderToDriver assignDepotOrderToDriver){
        return  depotOrderService.assignDepotOrderToDriver(assignDepotOrderToDriver);

    }

    @PostMapping("updateCartItemQuanity")
    public MinimalRes updateCartItemQuanity(DepotCartQuantityDto depotCartQuantityDto){
        return depotOrderService.updateCartItemQuanity(depotCartQuantityDto);
    }

}
