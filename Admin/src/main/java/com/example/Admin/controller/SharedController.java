package com.example.Admin.controller;

import com.example.Admin.dto.ExtendedRes;
import com.example.Admin.service.Components.Main.OrderInterface;
import com.example.Admin.service.Components.Main.RetailerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("api/shared")
    @AllArgsConstructor
    @CrossOrigin("*")
    public class SharedController {
        private final RetailerService retailerService;
        private final OrderInterface orderServices;

        @GetMapping("/All/Retailers")
        public ExtendedRes getAllRetailers(){
            return  retailerService.getAllRetailers();
        }
        @GetMapping("/getRetailerAndOrders/{retailerId}")
        public Object getRetailerAndOrders(@PathVariable Long retailerId){
            return orderServices.getRetailerAndOrders(retailerId);
        }
}
