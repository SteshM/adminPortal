package com.example.Admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
@Setter
@Getter
@Entity
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String fullName;
    private String password;
    private String contact;
    private boolean otp;
    private int tracker;
    @Column(unique = true)
    private String employeeNo;
    @ManyToMany
    private Collection<Profile> profiles = new ArrayList<>();
    private String picture;
    private boolean cloud;
    private String publicId;
    private boolean softDelete;
}
