package com.example.dto;

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
