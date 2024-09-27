package com.example.Admin.repository;

import com.example.Admin.dto.AnalyticsRequestDto;
import com.example.Admin.model.Analytics.CustomerAnalytics;
import com.example.Admin.model.Analytics.OrderAnalytics;
import com.example.Admin.model.Analytics.PaymentAnalytics;
import com.example.Admin.model.Analytics.ProductsAnalytics;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsCreteriaRepo {
    @Autowired
    EntityManager em;

    public List<PaymentAnalytics> getPaymentAnalytics(AnalyticsRequestDto paymentAnalyticsRequestDto){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PaymentAnalytics> query = cb.createQuery(PaymentAnalytics.class);
        Root<PaymentAnalytics> root = query.from(PaymentAnalytics.class);

        List<Predicate> predicates = new ArrayList<>();
        if(paymentAnalyticsRequestDto.getDate() != 0){
            Predicate datePredicate = cb.equal(root.get("date"), paymentAnalyticsRequestDto.getDate());
            predicates.add(datePredicate);
        }

        if(paymentAnalyticsRequestDto.getMonth() != 0){
            Predicate monthPredicate = cb.equal(root.get("month"), paymentAnalyticsRequestDto.getMonth());
            predicates.add(monthPredicate);
        }

        if(paymentAnalyticsRequestDto.getYear() != 0){
            Predicate yearPredicate = cb.equal(root.get("year"),paymentAnalyticsRequestDto.getYear());
            predicates.add(yearPredicate);
        }

        if(paymentAnalyticsRequestDto.getDepotId() != null && paymentAnalyticsRequestDto.getDepotId() != 0){
            Predicate depotIdPredicate = cb.equal(root.get("depotId"), paymentAnalyticsRequestDto.getDepotId());
            predicates.add(depotIdPredicate);
        }

        Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
        query.where(predicate);

        TypedQuery<PaymentAnalytics> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }



    public List<ProductsAnalytics> getProductsAnalytics(AnalyticsRequestDto paymentAnalyticsRequestDto){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductsAnalytics> query = cb.createQuery(ProductsAnalytics.class);
        Root<ProductsAnalytics> root = query.from(ProductsAnalytics.class);

        List<Predicate> predicates = new ArrayList<>();
        if(paymentAnalyticsRequestDto.getDate() != 0){
            Predicate datePredicate = cb.equal(root.get("date"), paymentAnalyticsRequestDto.getDate());
            predicates.add(datePredicate);
        }

        if(paymentAnalyticsRequestDto.getMonth() != 0){
            Predicate monthPredicate = cb.equal(root.get("month"), paymentAnalyticsRequestDto.getMonth());
            predicates.add(monthPredicate);
        }

        if(paymentAnalyticsRequestDto.getYear() != 0){
            Predicate yearPredicate = cb.equal(root.get("year"),paymentAnalyticsRequestDto.getYear());
            predicates.add(yearPredicate);
        }

        if(paymentAnalyticsRequestDto.getDepotId() != null && paymentAnalyticsRequestDto.getDepotId() != 0){
            Predicate depotIdPredicate = cb.equal(root.get("depotId"), paymentAnalyticsRequestDto.getDepotId());
            predicates.add(depotIdPredicate);
        }

        Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
        query.where(predicate);

        TypedQuery<ProductsAnalytics> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    public List<CustomerAnalytics> getCustomerAnalytics(AnalyticsRequestDto paymentAnalyticsRequestDto){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CustomerAnalytics> query = cb.createQuery(CustomerAnalytics.class);
        Root<CustomerAnalytics> root = query.from(CustomerAnalytics.class);

        List<Predicate> predicates = new ArrayList<>();
        if(paymentAnalyticsRequestDto.getDate() != 0){
            Predicate datePredicate = cb.equal(root.get("date"), paymentAnalyticsRequestDto.getDate());
            predicates.add(datePredicate);
        }

        if(paymentAnalyticsRequestDto.getMonth() != 0){
            Predicate monthPredicate = cb.equal(root.get("month"), paymentAnalyticsRequestDto.getMonth());
            predicates.add(monthPredicate);
        }

        if(paymentAnalyticsRequestDto.getYear() != 0){
            Predicate yearPredicate = cb.equal(root.get("year"),paymentAnalyticsRequestDto.getYear());
            predicates.add(yearPredicate);
        }

        // if(paymentAnalyticsRequestDto.getDepotId() != null || paymentAnalyticsRequestDto.getDepotId() != 0){
        //     Predicate depotIdPredicate = cb.equal(root.get("depotId"), paymentAnalyticsRequestDto.getDepotId());
        //     predicates.add(depotIdPredicate);
        // }

        Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
        query.where(predicate);

        TypedQuery<CustomerAnalytics> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    public List<OrderAnalytics> getOrderAnalytics(AnalyticsRequestDto paymentAnalyticsRequestDto){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderAnalytics> query = cb.createQuery(OrderAnalytics.class);
        Root<OrderAnalytics> root = query.from(OrderAnalytics.class);

        List<Predicate> predicates = new ArrayList<>();
        if(paymentAnalyticsRequestDto.getDate() != 0){
            Predicate datePredicate = cb.equal(root.get("date"), paymentAnalyticsRequestDto.getDate());
            predicates.add(datePredicate);
        }

        if(paymentAnalyticsRequestDto.getMonth() != 0){
            Predicate monthPredicate = cb.equal(root.get("month"), paymentAnalyticsRequestDto.getMonth());
            predicates.add(monthPredicate);
        }

        if(paymentAnalyticsRequestDto.getYear() != 0){
            Predicate yearPredicate = cb.equal(root.get("year"),paymentAnalyticsRequestDto.getYear());
            predicates.add(yearPredicate);
        }

        if(paymentAnalyticsRequestDto.getDepotId() != null && paymentAnalyticsRequestDto.getDepotId() != 0){
            Predicate depotIdPredicate = cb.equal(root.get("depotId"), paymentAnalyticsRequestDto.getDepotId());
            predicates.add(depotIdPredicate);
        }

        Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
        query.where(predicate);

        TypedQuery<OrderAnalytics> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

}
