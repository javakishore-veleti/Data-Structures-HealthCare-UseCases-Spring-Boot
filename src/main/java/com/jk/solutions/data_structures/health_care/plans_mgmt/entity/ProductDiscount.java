package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ProductDiscount")
public class ProductDiscount extends BaseEntity {

    @Column(name = "discount_id", nullable = false)
    private Long discountId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "discount_type", length = 50)
    private String discountType;
}
