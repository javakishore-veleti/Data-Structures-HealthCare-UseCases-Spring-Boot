package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ProductPrice")
public class ProductPrice extends BaseEntity {

    @Column(name = "price_id", nullable = false)
    private Long priceId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "price_amount", precision = 15, scale = 2)
    private BigDecimal priceAmount;

    @Column(name = "currency", length = 10)
    private String currency;
}
