package com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;

public interface GraphDependencyService {
    void topologicalSort(DSAPatternReq req, DSAPatternResp resp);
    void validateDependencyRules(DSAPatternReq req, DSAPatternResp resp);
    void traverseQualificationsDFSorBFS(DSAPatternReq req, String startFeature, DSAPatternResp resp);
    void clusterPlansUnionFind(DSAPatternReq req, DSAPatternResp resp);
    void computeUpgradePaths(DSAPatternReq req, DSAPatternResp resp);
}

