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

    private int maxDepth;              // For DFS/BFS depth-limiting or cutoff
    private boolean excludeCycles;     // Prevent revisits or loops or ignore paths that revisit nodes

    private boolean useWeights;        // whether to consider weights
    private int weightThreshold;       // only allow traversals below this threshold

}
