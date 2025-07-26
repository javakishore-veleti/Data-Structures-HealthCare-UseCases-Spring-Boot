package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ProductQualification")
public class ProductQualification extends BaseEntity{
    private Long qualificationId;
    private Long productId;
    private String country;
    private String county;
    private String channel;
}
