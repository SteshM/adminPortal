package com.example.Admin.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private Long userId;
    private  String fullName;
    private String employeeNo;
    private String contact;
}
