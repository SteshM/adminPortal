package com.example.model;

import com.example.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
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
