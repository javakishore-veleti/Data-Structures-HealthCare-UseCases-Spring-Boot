package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Account_Plan_Order")
public class AccountPlanOrder extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "account_number", nullable = false, length = 64)
    private String accountNumber;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "order_lines_count", nullable = false)
    private Integer orderLinesCount;

    @Column(name = "order_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal orderCost;

    @Column(name = "order_discount", precision = 15, scale = 2)
    private BigDecimal orderDiscount;

    @Column(name = "order_total_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal orderTotalCost;

    @Column(name = "order_status", nullable = false, length = 32)
    private String orderStatus;

    @Column(name = "order_canceled_date")
    private LocalDate orderCanceledDate;

    @Column(name = "order_fulfilled_date")
    private LocalDate orderFulfilledDate;

    // Getters and Setters (or use Lombok if preferred)
}
