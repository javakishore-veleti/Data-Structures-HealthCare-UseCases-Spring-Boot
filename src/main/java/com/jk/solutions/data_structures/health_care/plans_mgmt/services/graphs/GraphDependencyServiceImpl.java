package com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs;

import com.jk.solutions.data_structures.health_care.plans_mgmt.common.AlgorithmMethodType;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.ProductFeatureDependency;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.ProductFeatureDependencyRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@SuppressWarnings({"unused", "SpringJavaAutowiredFieldsWarningInspection"})
@Component
public class GraphDependencyServiceImpl implements GraphDependencyService {

    @Autowired
    private ProductFeatureDependencyRepository repository;

    /**
     Use Case: Enforce feature enablement order (e.g., pricing after base plan qualification).
     Algorithm: Topological Sort with methodType switch.

     Topological sort only works on Directed Acyclic Graphs (DAGs). That means:
     If any feature depends on another feature that eventually leads back to the first one → you get a cycle.
     */
    @SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
    @Override
    public void topologicalSort(DSAPatternReq req, DSAPatternResp resp) {
        String methodTypeStr = req.getMethodType();
        AlgorithmMethodType methodType;

        try {
            methodType = AlgorithmMethodType.valueOf(methodTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid method type: " + methodTypeStr);
        }

        switch (methodType) {
            case STANDARD -> topologicalSortStandard(req, resp);
            case MEMORY_EFFICIENT -> topologicalSortMemoryOptimized(req, resp);
            case DATABASE_OPTIMIZED -> topologicalSortDbDriven(req, resp);
            default -> {
                resp.setMessage("Unsupported method type for topological sort");
            }
        }
    }

    /**
     * STANDARD: Classic Kahn’s Algorithm
     * The method determines an order in which features must be enabled for a given product such that dependencies
     * are respected—i.e., no feature is enabled before its prerequisites.
     */
    private void topologicalSortStandard(DSAPatternReq req, DSAPatternResp resp) {
        // Fetches all feature dependencies (edges) for the given productId from the DB.
        String productId = req.getProductId(); // used as productId

        // Each record is a directed edge: from → to, meaning to depend on from.
        List<ProductFeatureDependency> edges = repository.findByProductId(productId);

        if (ObjectUtils.isEmpty(edges)) {
            resp.setMessage("No dependency data found for productId: " + productId);
            return;
        }

        // adjList: Adjacency list representing the graph structure.
        Map<String, List<String>> adjList = new HashMap<>();

        // inDegree: Count of incoming edges (prerequisites) for each node (feature).
        /*
        What is inDegree in a Graph?
        In graph theory: inDegree of a node = number of edges coming into that node.
        It tells you how many prerequisites that node (or feature) depends on.
         */
        Map<String, Integer> inDegree = new HashMap<>();

        // Populates adjList with all edges: fromFeature → List of dependentFeatures.
        // Tracks how many prerequisites (inDegree) each feature has.
        for (ProductFeatureDependency edge : edges) {
            String from = edge.getSourceFeatureCode();
            String to = edge.getDependentFeatureCode();

            adjList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            inDegree.putIfAbsent(from, 0);
        }

        // All features that don’t depend on anything are placed in the initial processing queue.
        Queue<String> queue = new ArrayDeque<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Remove features from the queue and add to the result list.
        // Reduce the inDegree of its dependent features.
        // If a dependent feature’s inDegree hits zero, add it to the queue.
        List<String> sortedOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String feature = queue.poll();
            sortedOrder.add(feature);

            for (String neighbor : adjList.getOrDefault(feature, Collections.emptyList())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // If not all nodes are processed, there's a cycle—which means some features depend on each other circularly (invalid in real-world dependencies).
        // Otherwise, a valid topological order is returned.
        if (sortedOrder.size() != inDegree.size()) {
            resp.setMessage("Cycle detected in dependency graph");
            resp.setResult(sortedOrder);
        } else {
            resp.setMessage("Topological sort completed");
            resp.setResult(sortedOrder);
        }
        resp.addResult("adjList", adjList);
        resp.addResult("inDegree", inDegree);
    }

    /**
     * MEMORY_EFFICIENT: Stream and reduce edges incrementally.
     */
    private void topologicalSortMemoryOptimized(DSAPatternReq req, DSAPatternResp resp) {
        // Delegating to standard for now – future: stream through large edge sets
        topologicalSortStandard(req, resp);
        resp.setMessage("Memory-efficient topological sort (delegated)");
    }

    /**
     * DATABASE_OPTIMIZED: Placeholder – recommend native recursive CTE in production
     */
    private void topologicalSortDbDriven(DSAPatternReq req, DSAPatternResp resp) {
        resp.setMessage("Topological sort via DB not implemented – recommend recursive CTE or graph DB");
        resp.setResult(List.of());
    }


    /*
    Business Purpose
    Ensure that all required features for a product are properly ordered and satisfied. For example, if Lambda
    depends on IAM, but IAM is not configured before Lambda, the system should flag that as a violation.

    Design Goals
    This method checks:
    - If all dependencies are reachable from root features.
    - If any missing features or out-of-order enablement exists.
    - Detect cyclic dependencies (if applicable).
     */
    @Override
    public void validateDependencyRules(DSAPatternReq req, DSAPatternResp resp) {

        String productId = req.getProductId();
        List<ProductFeatureDependency> edges = repository.findByProductId(productId);

        if (ObjectUtils.isEmpty(edges)) {
            resp.setMessage("No dependency rules found for productId: " + productId);
            return;
        }

        Map<String, List<String>> adjList = new HashMap<>();
        Set<String> allNodes = new HashSet<>();
        Set<String> dependentNodes = new HashSet<>();

        for (ProductFeatureDependency edge : edges) {
            String from = edge.getSourceFeatureCode();
            String to = edge.getDependentFeatureCode();

            adjList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allNodes.add(from);
            allNodes.add(to);
            dependentNodes.add(to);
        }

        // Root nodes: features that are not dependent on others
        Set<String> rootFeatures = new HashSet<>(allNodes);
        rootFeatures.removeAll(dependentNodes);

        Set<String> visited = new HashSet<>();
        List<String> visitOrder = new ArrayList<>();

        for (String root : rootFeatures) {
            dfs(root, adjList, visited, visitOrder);
        }

        if (visited.size() != allNodes.size()) {
            Set<String> missing = new HashSet<>(allNodes);
            missing.removeAll(visited);
            resp.setMessage("Invalid configuration: Unreachable features - " + missing);
            resp.setResult(visitOrder);
        } else {
            resp.setMessage("All dependency rules are valid and satisfied");
            resp.setResult(visitOrder);
        }
    }

    private void dfs(String feature, Map<String, List<String>> graph, Set<String> visited, List<String> result) {
        if (visited.contains(feature)) return;

        visited.add(feature);
        result.add(feature);
        for (String neighbor : graph.getOrDefault(feature, Collections.emptyList())) {
            dfs(neighbor, graph, visited, result);
        }
    }

    @Override
    public void traverseQualificationsDFSorBFS(DSAPatternReq req, String startFeature, DSAPatternResp resp) {

    }

    @Override
    public void clusterPlansUnionFind(DSAPatternReq req, DSAPatternResp resp) {

    }

    @Override
    public void computeUpgradePaths(DSAPatternReq req, DSAPatternResp resp) {

    }
}
