package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "AccountPlans")
public class AccountPlans extends BaseEntity {

    @Column(name = "account_number", nullable = false, length = 64)
    private String accountNumber;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "purchased_date")
    private LocalDate purchasedDate;

    @Column(name = "closed_date")
    private LocalDate closedDate;

    @Column(name = "canceled_date")
    private LocalDate canceledDate;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "version")
    private Integer version;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 64)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 64)
    private String updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Getters and setters or Lombok annotations can be added here
}
