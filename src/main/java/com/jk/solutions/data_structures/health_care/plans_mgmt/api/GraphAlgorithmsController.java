package com.jk.solutions.data_structures.health_care.plans_mgmt.api;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dsa/graphs")
public class GraphAlgorithmsController {

    @Autowired
    private GraphDependencyService graphDependencyService;

    @Autowired
    private PlanTransitionService planTransitionService;

    @Autowired
    private QualificationGraphService qualificationGraphService;

    @Autowired
    private GeoConnectivityService geoConnectivityService;

    @Autowired
    private PlanUpgradePathService upgradePathService;

    /**
     * Use Case: Enforce feature enablement order (e.g., pricing after base plan qualification).
     * Algorithm: Topological Sort
     */
    @PostMapping("/topo-sort")
    public ResponseEntity<DSAPatternResp> getFeatureEnablementOrder(@RequestBody DSAPatternReq req) {
        DSAPatternResp resp = new DSAPatternResp();
        graphDependencyService.topologicalSort(req, resp);

        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Determine connected qualification dependencies across multiple product features.
     * Algorithm: DFS or BFS
     */
    @PostMapping("/qual-dependency")
    public ResponseEntity<DSAPatternResp> exploreQualificationDependencies(@RequestBody DSAPatternReq req) {
        DSAPatternResp resp = new DSAPatternResp();
        qualificationGraphService.dfsOrBfsTraverse(req, resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Find optimal transition paths between plans based on eligibility or cost-efficiency.
     * Algorithm: Dijkstra’s Algorithm
     */
    @PostMapping("/plan-transition")
    public ResponseEntity<DSAPatternResp> getBestPlanTransitionPath(@RequestBody DSAPatternReq req) {
        DSAPatternResp resp = new DSAPatternResp();
        planTransitionService.findOptimalPath(req, resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Cluster related plans or features with shared qualifications to identify isolated or core offerings.
     * Algorithm: Union-Find (Disjoint Set)
     */
    @PostMapping("/group-qualified-plans")
    public ResponseEntity<DSAPatternResp> getGroupedPlans(@RequestBody DSAPatternReq req) {
        DSAPatternResp resp = new DSAPatternResp();
        planTransitionService.clusterQualifiedPlans(req,resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Analyze provider coverage or regional availability by traversing county/country-provider graphs.
     * Algorithm: Graph Traversal (BFS)
     */
    @PostMapping("/geo-connectivity")
    public ResponseEntity<DSAPatternResp> analyzeGeoConnectivity(@RequestBody DSAPatternReq req) {
        DSAPatternResp resp = new DSAPatternResp();
        geoConnectivityService.analyzeCoverageGraph(req, resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Model and validate upgrade chains among tiered insurance plans.
     * Algorithm: BFS or DFS
     */
    @PostMapping("/upgrade-path")
    public ResponseEntity<DSAPatternResp> getUpgradePaths(@RequestBody DSAPatternReq req) {
        DSAPatternResp resp = new DSAPatternResp();
        upgradePathService.evaluateUpgradeChains(req,resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Validate correct enablement sequence of products/features based on business rules.
     * Algorithm: Dependency Graph
     */
    @PostMapping("/dependency-validate")
    public ResponseEntity<DSAPatternResp> validateDependencyGraph(@RequestBody DSAPatternReq req) {
        DSAPatternResp resp = new DSAPatternResp();
        graphDependencyService.validateDependencyRules(req, resp);
        return ResponseEntity.ok(resp);
    }
}