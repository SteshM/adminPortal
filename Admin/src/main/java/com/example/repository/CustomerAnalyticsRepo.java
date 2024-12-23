package com.example.repository;

import com.example.model.Analytics.CustomerAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAnalyticsRepo extends JpaRepository<CustomerAnalytics,Long> {
    public CustomerAnalytics findByDate(int date);
    public List<CustomerAnalyticsRepo> findByMonth(int month);
    public List<CustomerAnalytics> findByYear(int year);
}
