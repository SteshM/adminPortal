package com.example.Admin.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSuperAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    private String adminName;
    private String email;
    private int depotsAssigned;
    private String profilePic;
    private boolean cloud;
    private String createdOn;
    private String employeeNo;
    private String contact;
}
