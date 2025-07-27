package com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;

public interface GeoConnectivityService {
    void analyzeCoverageGraph(DSAPatternReq req, DSAPatternResp resp);
}
