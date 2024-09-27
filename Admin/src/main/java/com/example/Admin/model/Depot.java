package com.example.Admin.model;

import com.example.Admin.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Depot {
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
