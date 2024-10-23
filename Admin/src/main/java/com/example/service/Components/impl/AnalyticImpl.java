package com.example.service.Components.impl;

import com.example.dto.AnalyticsRequestDto;
import com.example.dto.ExtendedRes;
import com.example.model.Analytics.CustomerAnalytics;
import com.example.model.Analytics.OrderAnalytics;
import com.example.model.Analytics.PaymentAnalytics;
import com.example.model.Analytics.ProductsAnalytics;
//import com.example.Admin.repository.*;
import com.example.repository.*;
import com.example.service.Components.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticImpl implements AnalyticGuide{
    private final CustomerAnalyticsRepo customerAnalyticsRepo;
    private final ProductAnalyticsRepo productAnalyticsRepo;
    private final OrderAnalyticsRepo orderAnalyticsRepo;
    private final PaymentAnalyticsRepo paymentAnalyticsRepo;
    private final AnalyticsCreteriaRepo analyticsCreteriaRepo;

    @Override
    public boolean addCustomer() {
        CustomerAnalytics customersAnalytics = customerAnalyticsRepo.findByDate(DateUtils.day());
        if(customersAnalytics == null){
            customersAnalytics = new CustomerAnalytics();
            customersAnalytics.setDailyCount(1);
            customersAnalytics.setYear(DateUtils.year());
            customersAnalytics.setMonth(DateUtils.month());
            customersAnalytics.setDate(DateUtils.day());
            customerAnalyticsRepo.save(customersAnalytics);
        }else{
            customersAnalytics.setDailyCount(customersAnalytics.getDailyCount()+1);
            customerAnalyticsRepo.save(customersAnalytics);
        }
        return true;
    }

    @Override
    public boolean addOrder(Long depotId) {
        OrderAnalytics orderAnalytics = orderAnalyticsRepo.findByDateAndDepotId(DateUtils.day(), depotId);
        if(orderAnalytics == null){
            orderAnalytics = new OrderAnalytics();
            orderAnalytics.setDailyCount(1);
            orderAnalytics.setDate(DateUtils.day());
            orderAnalytics.setMonth(DateUtils.month());
            orderAnalytics.setYear(DateUtils.year());
            orderAnalytics.setDepotId(depotId);
            orderAnalyticsRepo.save(orderAnalytics);
        }else{
            orderAnalytics.setDailyCount(orderAnalytics.getDailyCount()+1);
            orderAnalyticsRepo.save(orderAnalytics);
        }
        return true;
    }

    @Override
    public boolean addProduct(Long depotId, int products) {
        ProductsAnalytics productsAnalytics = productAnalyticsRepo.findByDate(DateUtils.day());
        if(productsAnalytics == null){
            productsAnalytics = new ProductsAnalytics();
            productsAnalytics.setDailyCount(products);
            productsAnalytics.setYear(DateUtils.year());
            productsAnalytics.setMonth(DateUtils.month());
            productsAnalytics.setDate(DateUtils.day());
            productsAnalytics.setDepotId(depotId);
            productAnalyticsRepo.save(productsAnalytics);
        }else{
            productsAnalytics.setDailyCount(productsAnalytics.getDailyCount() + products);
            productAnalyticsRepo.save(productsAnalytics);
        }
        return true;
    }

    @Override
    public boolean addPayment(Long depotId, float amount) {
        PaymentAnalytics paymentAnalytics = paymentAnalyticsRepo.findByDateAndDepotId(DateUtils.day(), depotId);
        if(paymentAnalytics == null){
            paymentAnalytics = new PaymentAnalytics();
            paymentAnalytics.setDailyCount(amount);
            paymentAnalytics.setDate(DateUtils.day());
            paymentAnalytics.setMonth(DateUtils.month());
            paymentAnalytics.setYear(DateUtils.year());
            paymentAnalytics.setDepotId(depotId);
            paymentAnalyticsRepo.save(paymentAnalytics);
        }else{
            paymentAnalytics.setDailyCount(paymentAnalytics.getDailyCount()+1);
            paymentAnalyticsRepo.save(paymentAnalytics);
        }
        return true;
    }

    @Override
    public ExtendedRes getCustomers(AnalyticsRequestDto analyticsRequestDto) {
        return ExtendedRes.builder()
                .status(200)
                .message("Analytics for customers")
                .body(analyticsCreteriaRepo.getCustomerAnalytics(analyticsRequestDto))
                .build();
    }

    @Override
    public ExtendedRes getOrders(AnalyticsRequestDto analyticsRequestDto) {
        return ExtendedRes.builder()
                .status(200)
                .message("Analytics for orders")
                .body(analyticsCreteriaRepo.getOrderAnalytics(analyticsRequestDto))
                .build();
    }

    @Override
    public ExtendedRes getProducts(AnalyticsRequestDto analyticsRequestDto) {
        return ExtendedRes.builder()
                .status(200)
                .message("Analytics for products")
                .body(analyticsCreteriaRepo.getProductsAnalytics(analyticsRequestDto))
                .build();
    }

    @Override
    public ExtendedRes getPayments(AnalyticsRequestDto analyticsRequestDto) {
        return ExtendedRes.builder()
                .status(200)
                .message("Analytics for payments")
                .body(analyticsCreteriaRepo.getPaymentAnalytics(analyticsRequestDto))
                .build();
    }
}
