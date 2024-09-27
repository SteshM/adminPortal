package com.example.Admin.model;

import com.example.Admin.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DepotAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depotId;
    private String name;
    @OneToOne
    private Location location;
    private String picture;
    private boolean cloud;
    private String publicId;
    private String createdOn = DateUtils.dateNowString();
}
