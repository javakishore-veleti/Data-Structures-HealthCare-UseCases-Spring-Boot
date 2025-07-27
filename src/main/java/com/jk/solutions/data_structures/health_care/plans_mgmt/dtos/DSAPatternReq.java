package com.jk.solutions.data_structures.health_care.plans_mgmt.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DSAPatternReq {

    private String productId;
    private String accountNbr;
    private String currentPlan;
    private int windowSize;
    private double targetSum;
    private String methodType;
    private BigDecimal budget;
    private int indexFrom;
    private int indexTo;

}
