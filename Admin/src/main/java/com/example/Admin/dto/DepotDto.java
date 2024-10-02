package com.example.Admin.dto;

import com.example.Admin.model.Location;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DepotDto {
    private Long depotId;
    private String name;

    private Location location;
}
