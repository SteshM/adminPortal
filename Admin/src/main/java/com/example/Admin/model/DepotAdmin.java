package com.example.Admin.model;

import com.example.Admin.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class DepotAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="userId")
    private MyUser myUser;
    @ManyToOne
    @JoinColumn(name = "depotId")
    private Depot depot;
    private String assignedOn = DateUtils.dateNowString();
}

