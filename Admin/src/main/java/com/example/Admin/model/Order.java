package com.example.Admin.model;

import com.example.Admin.enums.OrderStatus;
import com.example.Admin.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @ManyToOne
    @JoinColumn(name="retailerId")
    private  MyUser retailer;
    @ManyToOne
    @JoinColumn(name="shopId")
    private Shop shop;
    @ManyToOne
    @JoinColumn(name="depotId")
    private Depot depot;
    @ManyToOne
    @JoinColumn(name = "driverId")
    private MyUser driver;
    private OrderStatus status;
    private String orderName;
    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private String orderDate = DateUtils.dateNowString();
    private String deliveryCode;
    //private List<Integer> productIds;


    @LastModifiedDate
    @Column(insertable = false)
    private String lastModified = DateUtils.dateNowString();

    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Long lastModifiedBy;
}

