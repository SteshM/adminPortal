package com.example.Admin.model;

import com.example.Admin.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetailerOrder {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long orderId;
    private long retailerId;
    private String orderName;
    private String retailerName;
    private String depotName;
    private String driverName;
    private String driverEmail;
    private String orderDate;
    private String dateCreated;
    private int orders;
    private String shopName;
    private OrderStatus status;

}
