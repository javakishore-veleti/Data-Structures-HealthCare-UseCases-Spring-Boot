package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ProductPrice")
public class ProductPrice extends BaseEntity {

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "price_amount",nullable = false)
    private BigDecimal priceAmount;

    @Column(name = "currency")
    private String currency;
}
