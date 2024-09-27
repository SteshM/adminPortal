package com.example.Admin.dto;

import lombok.Data;

@Data
public class AnalyticsRequestDto {
    private int date;
    private int month;
    private int year;
    private Long depotId;
}
