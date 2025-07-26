package com.jk.solutions.data_structures.health_care.plans_mgmt.dtos;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Value
public class DSAPatternReq {

    private String accountNbr;
    private int windowSize;

}
