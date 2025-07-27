package com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;

public interface PlanTransitionService {
    void findOptimalPath(DSAPatternReq req, DSAPatternResp resp);

    void clusterQualifiedPlans(DSAPatternReq req, DSAPatternResp resp);
}
