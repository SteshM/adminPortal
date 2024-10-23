package com.example.controller;


import com.example.dto.ExtendedRes;
import com.example.service.Components.Main.DepotOrderService;
import com.example.service.Components.Main.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("v1/general")
@RequiredArgsConstructor
public class GeneralController {
    private final ProductService productService;
    private final DepotOrderService depotOrderService;
    // private final OrderService orderService;
    // private final InventoryService inventoryService;

    @GetMapping("getProducts")
    public ExtendedRes getProducts() {
        return productService.getDisplayProducts();
    }

    @GetMapping("getProductAttributes/{productId}")
    public ExtendedRes getProductAttributes(@PathVariable Long productId) {
        return productService.getProductQAttributes(productId);
    }

    @GetMapping("getProductById/{productId}")
    public ExtendedRes getProductById(@PathVariable Long productId) {
        return productService.findById(productId);

    }
    @GetMapping("getOrderItems/{depotOrderId}")
    public ExtendedRes getOrderItems(@PathVariable Long depotOrderId) {
        return depotOrderService.getOrderItems(depotOrderId);
    }

}





