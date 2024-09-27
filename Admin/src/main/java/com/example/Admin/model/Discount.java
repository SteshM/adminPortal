package com.example.Admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity

public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float discountAmount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}

