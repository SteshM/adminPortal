package com.example.Admin.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinimalRes {
    private int status;
    private boolean success;
    private String message;
}
