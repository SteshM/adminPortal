package com.example.Admin.dto;

import lombok.Data;

@Data
public class ConfirmationDto {
    private  Long orderId;
    private String confirmationCode;
}
