package com.example.Admin.model;

import com.example.Admin.enums.OrderStatus;
import com.example.Admin.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class DepotOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "depotId")
    private Depot depot;
    //changes to driver later
    @ManyToOne
    @JoinColumn(name="userId")
    private MyUser myUser;
    @ManyToOne
    @JoinColumn(name = "assignedDepotId")
    private Depot assignedDepot;
    private String orderName;
    private String deliveryCode;
    private OrderStatus status;
    private String orderDate = DateUtils.dateNowString();
}
