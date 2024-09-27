package com.example.Admin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Inventory {
    @Id
    @GeneratedValue
    private Long inventoryId;
    private Long productId;

    //    private OrderItem orderItemId;
    private Long depotId;
    private int initialQuantity;
    private int quantitySold;
    private int threshold;
    private int max;
    public int getQuantityRemaining() {
        return initialQuantity - quantitySold;
    }

}
