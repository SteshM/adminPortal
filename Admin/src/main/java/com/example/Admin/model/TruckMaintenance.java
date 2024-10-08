package com.example.Admin.model;

import com.example.Admin.enums.MaintenanceStatus;
import com.example.Admin.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TruckMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "truckId")
    private Truck truck;
    @ManyToOne
    @JoinColumn(name="userId")
    private MyUser myUser;
    private MaintenanceStatus maintenanceStatus;
    private String maintenanceApprovalDate;
    private String maintenanceDate = DateUtils.dateNowString();
    private float cost;
    private String details;
}
