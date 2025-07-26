package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;

public interface PrefixSumPlanAnalyzer {

    void analyzeCumulativeTotalCostRange(DSAPatternReq req, DSAPatternResp resp);
}
