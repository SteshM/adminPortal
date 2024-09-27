package com.example.Admin.model.Analytics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ProductsAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int date;
    private Long depotId;
    private int month;
    private int year;
    private float dailyCount;
}
