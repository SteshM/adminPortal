package com.example.Admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDto {
    public String productName;
    public long productId;
    public String productAttributeName;
    public long productAttributeId;
    public int quantity;
    private Long depotId;
    private String depotName;
    public boolean cloud;
    public String picture;

}
