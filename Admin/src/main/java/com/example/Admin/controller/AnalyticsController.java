package com.example.Admin.controller;

import com.example.Admin.dto.AnalyticsRequestDto;
import com.example.Admin.dto.ExtendedRes;
import com.example.Admin.service.Components.impl.AnalyticGuide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/analyics")
public class AnalyticsController {
    @Autowired
    AnalyticGuide analyticGuide;

    @PostMapping("getCustomers")
    public ExtendedRes getCustomers(@RequestBody AnalyticsRequestDto analyticsRequestDto){
        return analyticGuide.getCustomers(analyticsRequestDto);
    }

    @PostMapping("getOrders")
    public ExtendedRes getOrders(@RequestBody AnalyticsRequestDto analyticsRequestDto){
        return analyticGuide.getOrders(analyticsRequestDto);
    }

    @PostMapping("getProducts")
    public ExtendedRes getProducts(@RequestBody AnalyticsRequestDto analyticsRequestDto){
        return analyticGuide.getProducts(analyticsRequestDto);
    }


    @PostMapping("getPayments")
    public ExtendedRes getPayments(@RequestBody AnalyticsRequestDto analyticsRequestDto){
        return analyticGuide.getPayments(analyticsRequestDto);
    }

}


