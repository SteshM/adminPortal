package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedRes {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object body;
}
