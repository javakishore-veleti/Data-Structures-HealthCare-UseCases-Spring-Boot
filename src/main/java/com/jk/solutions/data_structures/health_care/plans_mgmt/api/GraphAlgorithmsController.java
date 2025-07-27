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

    /**
     * Use Case: Enforce feature enablement order (e.g., pricing after base plan).
     * Algorithm: Topological Sort
     */
    @GetMapping("/topo-sort")
    public ResponseEntity<DSAPatternResp> getTopologicalFeatureOrder(
            @RequestParam("productId") String productId,
            @RequestParam(name = "methodType", defaultValue = "standard") String methodType) {

        DSAPatternReq req = DSAPatternReq.builder().productId(productId).methodType(methodType).build();
        DSAPatternResp resp = new DSAPatternResp();
        graphDependencyService.topologicalSort(req, resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Validate dependency rules between features.
     * Algorithm: Graph Traversal (DFS or custom rules)
     */
    @GetMapping("/dependency-validation")
    public ResponseEntity<DSAPatternResp> validateFeatureDependencyRules(
            @RequestParam("productId") String productId,
            @RequestParam(name = "methodType", defaultValue = "standard") String methodType) {

        DSAPatternReq req = DSAPatternReq.builder().productId(productId).methodType(methodType).build();
        DSAPatternResp resp = new DSAPatternResp();
        graphDependencyService.validateDependencyRules(req, resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Discover qualification dependency paths.
     * Algorithm: BFS / DFS
     */
    @GetMapping("/traverse-qualifications")
    public ResponseEntity<DSAPatternResp> traverseQualificationGraph(
            @RequestParam("productId") String productId,
            @RequestParam("startFeature") String startFeature,
            @RequestParam(name = "methodType", defaultValue = "standard") String methodType) {

        DSAPatternReq req = DSAPatternReq.builder()
                .productId(productId)
                .windowSize(0) // reuse field if needed
                .methodType(methodType)
                .build();

        DSAPatternResp resp = new DSAPatternResp();
        graphDependencyService.traverseQualificationsDFSorBFS(req, startFeature, resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Cluster related plans by shared dependencies.
     * Algorithm: Union-Find (Disjoint Sets)
     */
    @GetMapping("/union-find-clustering")
    public ResponseEntity<DSAPatternResp> clusterPlansUsingUnionFind(
            @RequestParam(name = "methodType", defaultValue = "standard") String methodType) {

        DSAPatternReq req = DSAPatternReq.builder()
                .methodType(methodType)
                .build();

        DSAPatternResp resp = new DSAPatternResp();
        graphDependencyService.clusterPlansUnionFind(req, resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Use Case: Suggest valid plan upgrade paths.
     * Algorithm: BFS / DFS
     */
    @GetMapping("/plan-upgrade-paths")
    public ResponseEntity<DSAPatternResp> getPlanUpgradePaths(
            @RequestParam("accountNbr") String accountNbr,
            @RequestParam("currentPlan") String currentPlan,
            @RequestParam(name = "methodType", defaultValue = "standard") String methodType) {

        DSAPatternReq req = DSAPatternReq.builder()
                .accountNbr(accountNbr)
                .methodType(methodType)
                .currentPlan(currentPlan)
                .build();

        DSAPatternResp resp = new DSAPatternResp();
        graphDependencyService.computeUpgradePaths(req, resp);
        return ResponseEntity.ok(resp);
    }
}