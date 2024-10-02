package com.example.Admin.controller;

import com.example.Admin.dto.*;
import com.example.Admin.enums.OrderStatus;
import com.example.Admin.model.QuantityAttribute;
import com.example.Admin.service.Components.Main.*;
import com.example.Admin.service.Components.utils.MyDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



    @RestController
    @RequestMapping("/super")
    @CrossOrigin("*")
    @AllArgsConstructor
    public class SuperAdminController{
        private  final AdminService adminService;
        private final DepotService depotService;
        private  final OrderService orderService;
        private final DriverService driverService;
        private final ProductService productService;
        private final OrderInterface orderInterface;
        private final TruckServices truckServices;
        private final DepotOrderService depotOrderService;


        @PostMapping("/assignAdmin/depot")
        public MinimalRes assignAdmin(@RequestBody AssignDepotAdmin depotAdmin){
            return adminService.assignDepotAdmin(depotAdmin);

        }
        @GetMapping("/admin/{id}")
        public ExtendedRes getAdminById(@PathVariable Long id) {
            return  adminService.getAdminById(id);

        }
        @PostMapping("restock")
        public  MinimalRes restock(@RequestBody RestockDto restockDto){
            return adminService.restock(restockDto);
        }
        @GetMapping("/admins")
        public ExtendedRes getAllAdmins() {
            return adminService.getAllAdmins();
        }
        @GetMapping("/superadmins")
        public ExtendedRes getAllSuperAdmins() {
            return adminService.getAllSuperAdmins();
        }
        @PostMapping("update/admin")
        public ExtendedRes  updateAdmin(
                @RequestBody UserUpdateDto adminDto){
            return adminService.updateById(adminDto);
        }

        @PostMapping("/new/depot")
        public MinimalRes createDepot(@RequestBody DepotDto depotdto ){
            return depotService.createDepot(depotdto);

        }
        @GetMapping("/depot/{id}")
        public ExtendedRes getDepotById(@PathVariable Long id) {
            return depotService.findDepotById(id);

        }

        @GetMapping("/depots")
        public ExtendedRes getAllDepots() {
            return depotService.findAllDepots();

        }
        @PostMapping("/update/depot/{id}")
        public ExtendedRes updateDepots(@PathVariable Long id,
                                        @RequestBody DepotDto depot ){
            depot.setDepotId(id);
            return depotService.updateById(depot);


        }
        @PostMapping("/assign/AssignOrderToDepot")
        public  MinimalRes assignOrders(@RequestBody AssignOrderToDepot assign){
            return  orderService.assignOrderToDepot(assign);

        }


        @PostMapping("/assign/AssigneDepotOrderToDepot")
        public  MinimalRes assignDepotOrders(@RequestBody AssigneDepotOrderToDepot assigneDepotOrderToDepot){
            return  depotOrderService.assignDepotOrderToDepot(assigneDepotOrderToDepot);

        }
//@GetMapping("/{orderId}/depots")
//public ResponseEntity<List<DepotDistance>> getDepotsWithDistances(@PathVariable Long orderId) {
//    List<DepotDistance> depotDistances = depotService.getDepotsWithDistances(orderId);
//    return new ResponseEntity<>(depotDistances, HttpStatus.OK);
//

        // @GetMapping("/ListOfDepots/{orderId}")
        // public ResponseEntity<List<DepotDistance>> getDepotsWithDistance(@PathVariable Long orderId){
        //     List<DepotDistance> depotDistances = depotServiceImpl.getDepotsWithDistances(orderId);
        //     return new ResponseEntity<>(depotDistances, HttpStatus.OK);

        // }


        @GetMapping("suggestedDepots/{orderId}")
        public Object getSuggestedDepots(@PathVariable Long orderId){

            return orderService.getDepots(orderId);
        }

        @GetMapping("/getdriver/{id}")
        public ExtendedRes findById(@PathVariable Long id){
            return driverService.findDriverById(id);


        }
        @GetMapping("/drivers")
        public ExtendedRes findAllDrivers(){
            return driverService.findAllDrivers();

        }
        @GetMapping("/deleted/drivers")
        public ExtendedRes findAllDeletedDrivers(){
            return driverService.findAllDeletedDrivers();
        }

        @GetMapping("getUnallocated/drivers")
        public ExtendedRes findAllUnallocatedDrivers(){
            return driverService.getUnallocatedDrivers();

        }


        @PostMapping("update/driver")
        public ExtendedRes updateDriver(@RequestBody DriverUpdateDto driver ){
            return driverService.updateById(driver);
        }
        @GetMapping("delete/driver/{id}")
        public MinimalRes deleteById(@PathVariable Long id){
            return  driverService.deleteById(id);

        }

        @GetMapping("admin/getAssignedDepots/{adminId}")
        public ExtendedRes getAssignedDepots(@PathVariable Long adminId){
            return  adminService.getAdminDepots(adminId);

        }

        @PostMapping("/driver/assignToDepot")
        public MinimalRes assignDriverToDepot(@RequestBody DriverAssignDto driverAssignDto){
            return  driverService.assignDriverToDepot(driverAssignDto);

        }

        //  add products to the system
        @PostMapping("/product")
        public  MinimalRes addProduct(@RequestBody ProductsDto product){
            return productService.createProduct(product);

        }
        @GetMapping("/product/{id}")
        public ExtendedRes getById(@PathVariable Long id){
            return productService.findById(id);

        }
//        @Operation(
//                description = "{\n" + //
//                        "    \"attrName\": \"exampleName\",\n" + //
//                        "    \"productId\": 123456789,\n" + //
//                        "    \"price\": 19.99\n" + //
//                        "} is the data and there will be file upload as file"
//        )
        @PostMapping("/product/addAttribute")
        public MinimalRes addAttribute(@RequestParam("data") String data,@RequestParam("file") MultipartFile file){
            AddAttributeDto addAttributeDto = MyDtoMapper.stringToClass(data, AddAttributeDto.class);
            return productService.addQAttribute(addAttributeDto, file);
        }
        @PostMapping("/updateProduct")
        public  ExtendedRes updateProduct(@RequestBody ProductsDto product){
            return productService.updateById(product);
        }
        @GetMapping("delete/product/{id}")
        public MinimalRes deleteProductById(@PathVariable Long id){

            return productService.deleteById(id);
        }
        @PostMapping("updateAttribute")
        public ExtendedRes updateProductAttributes(
                @RequestBody QuantityAttribute attribute){
            attribute.getQuantityAttId();
            return productService.updateProductQAttById(attribute);
        }
        @GetMapping("/getRetailersAndOrders")
        public ExtendedRes getRetailersOrders(){
            return adminService.getRetailersOrders();
        }

        @GetMapping("getOrdersInDepot/{depotId}")
        public ExtendedRes getOrdersInDepot(@PathVariable Long depotId){
            return orderInterface.getDepotOrders(depotId);
        }
        @GetMapping("getStockInDepot/{depotId}")
        public ExtendedRes stockInDepot(@PathVariable Long depotId){
            return depotService.getDepotStockDetails(depotId);
        }
        @GetMapping("getDepotDrivers/{depotId}")
        public ExtendedRes getDepotDrivers(@PathVariable Long depotId){
            return depotService.getDepotDrivers(depotId);
        }

//        @Operation(
//                description = "The example data body is: {\n" + //
//                        "  \"truckNo\": \"TX1234\",\n" + //
//                        "  \"make\": \"Ford\",\n" + //
//                        "  \"model\": \"F-150\",\n" + //
//                        "  \"year\": 2022,\n" + //
//                        "  \"color\": \"Blue\",\n" + //
//                        "  \"loadCapacity\": 1000.5,\n" + //
//                        "  \"fuelCapacity\": 30.0,\n" + //
//                        "  \"fuelType\": \"Diesel\"\n" + //
//                        "}"
//        )
        @PostMapping("createTruck")
        public MinimalRes createTruck(@RequestPart("data") String data,@RequestPart("images") List<MultipartFile> images) {
            return truckServices.createTruck(data, images);
        }
//
//        @Operation(
//                description = "The id must be included for updates"
//        )
        @PostMapping("updateTruck")
        public MinimalRes updateTruck(@RequestBody TruckUpdateDto truckDto) {
            return truckServices.updateTruck(truckDto);
        }

        @GetMapping("getAllTrucks")
        public ExtendedRes getTrucks() {
            return truckServices.getTrucks();
        }

        @GetMapping("getTrucksWithNoDriver")
        public ExtendedRes getTruckWithNoDrivers() {
            return truckServices.getTruckWithNoDrivers();
        }

        @GetMapping("getDriversWithNoTrucks")
        public ExtendedRes getDriversWithNoTrucks(){
            return adminService.getdriversWithNoTrucks();
        }

        @GetMapping("getUnassignedDepotOrders")
        public ExtendedRes getUnassignedDepotOrders(){
            return depotOrderService.getDepotOrders(OrderStatus.PENDING);
        }

        @PostMapping("assignTruck")
        public MinimalRes assignTruckTODriver(@RequestBody AssignTruckDto assignTruckDto){
            return truckServices.assignTruckToDriver(assignTruckDto);
        }

        @PostMapping("addOffer")
        public MinimalRes addOffer(@RequestBody OfferDto offerDto){
            return productService.addOffer(offerDto);
        }




}
