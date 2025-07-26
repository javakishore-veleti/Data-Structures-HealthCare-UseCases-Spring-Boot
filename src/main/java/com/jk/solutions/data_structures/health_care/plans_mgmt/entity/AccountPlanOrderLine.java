package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "AccountPlanOrderLine")
public class AccountPlanOrderLine extends BaseEntity {

    @Column(name = "order_line_id", nullable = false)
    private Long orderLineId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "discount", precision = 15, scale = 2)
    private BigDecimal discount;

    @Column(name = "total_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "payment_mode", length = 32)
    private String paymentMode;

    @Column(name = "payment_id", length = 64)
    private String paymentId;

    @Column(name = "order_line_created_dt")
    private LocalDateTime orderLineCreatedDt;

    // Getters and setters (or use Lombok)
}
