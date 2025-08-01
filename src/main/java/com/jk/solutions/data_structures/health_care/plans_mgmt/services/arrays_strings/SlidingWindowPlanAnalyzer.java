package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;

import java.math.BigDecimal;

public interface SlidingWindowPlanAnalyzer {

     void getAccountPurchasesMaxCostInWindowWithJPAStreams(DSAPatternReq req, DSAPatternResp resp) ;

     void populateSyntheticOrders(String accountNbr, int days, BigDecimal baseAmount);
}
