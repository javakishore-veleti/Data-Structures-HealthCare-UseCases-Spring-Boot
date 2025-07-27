package com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs;

import com.jk.solutions.data_structures.health_care.plans_mgmt.common.AlgorithmMethodType;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.UnionFind;
import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.ProductFeatureDependency;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.ProductFeatureDependencyRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        String productId = req.getProductId();

        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> adjList = new HashMap<>();

        try (Stream<ProductFeatureDependency> edgeStream = repository.streamDependenciesByProductId(productId)) {


            edgeStream.forEach(edge -> {
                String from = edge.getSourceFeatureCode();
                String to = edge.getDependentFeatureCode();

                adjList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
                inDegree.putIfAbsent(from, 0);
            });

            Queue<String> queue = new ArrayDeque<>();
            for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
                if (entry.getValue() == 0) queue.add(entry.getKey());
            }

            List<String> result = new ArrayList<>();
            while (!queue.isEmpty()) {
                String feature = queue.poll();
                result.add(feature);
                for (String neighbor : adjList.getOrDefault(feature, List.of())) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) queue.add(neighbor);
                }
            }

            if (result.size() != inDegree.size()) {
                resp.setMessage("Cycle detected (streamed)");
            } else {
                resp.setMessage("Topological sort completed with memory optimization");
                resp.setResult(result);
            }

        } catch (Exception ex) {
            resp.setMessage("Error during memory-optimized sort: " + ex.getMessage());
        }

        resp.addResult("adjList", adjList);
        resp.addResult("inDegree", inDegree);
    }

    /**
     * DATABASE_OPTIMIZED: Placeholder – recommend native recursive CTE in production
     */
    private void topologicalSortDbDriven(DSAPatternReq req, DSAPatternResp resp) {
        String productId = req.getProductId();

        try {

            List<Object[]> rows = repository.findTopologicallySortedFeaturesWithDepth(productId);
            List<String> sortedFeatures = rows.stream()
                    .map(row -> (String) row[0])
                    .distinct()
                    .toList();

            if (sortedFeatures.isEmpty()) {
                resp.setMessage("No dependencies found or possible cycle detected.");
            } else {
                resp.setResult(sortedFeatures);
                resp.setMessage("Topological sort completed via database recursive CTE.");
            }

            resp.addResult("sortedFeatures", sortedFeatures);

        } catch (Exception e) {
            resp.setMessage("Error during DB-driven topological sort: " + e.getMessage());
            resp.setResult(Collections.emptyList());
        }
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

        String productId = req.getProductId();
        String methodType = req.getMethodType();

        List<ProductFeatureDependency> edges = repository.findByProductId(productId);

        if (ObjectUtils.isEmpty(edges)) {
            resp.setMessage("No dependencies found for productId: " + productId);
            return;
        }

        // Build adjacency list
        Map<String, List<String>> adjList = new HashMap<>();
        for (ProductFeatureDependency edge : edges) {
            adjList.computeIfAbsent(edge.getSourceFeatureCode(), k -> new ArrayList<>())
                    .add(edge.getDependentFeatureCode());
        }

        Set<String> visited = new LinkedHashSet<>();

        if ("dfs".equalsIgnoreCase(methodType)) {
            dfsTraversal(startFeature, adjList, visited);
            resp.setMessage("Traversed using DFS");
        } else if ("bfs".equalsIgnoreCase(methodType)) {
            bfsTraversal(startFeature, adjList, visited);
            resp.setMessage("Traversed using BFS");
        } else {
            resp.setMessage("Unsupported methodType: " + methodType + " (expected: dfs or bfs)");
            return;
        }

        resp.setResult(new ArrayList<>(visited));

    }

    private void dfsTraversal(String feature, Map<String, List<String>> adjList, Set<String> visited) {
        if (!visited.add(feature)) return;
        for (String neighbor : adjList.getOrDefault(feature, Collections.emptyList())) {
            dfsTraversal(neighbor, adjList, visited);
        }
    }

    private void bfsTraversal(String start, Map<String, List<String>> adjList, Set<String> visited) {
        Queue<String> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : adjList.getOrDefault(current, Collections.emptyList())) {
                if (visited.add(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
    }

    /*
    Use Case: Cluster related plans with shared qualifications (e.g., features or eligibility rules) to identify isolated or core offerings.
    Algorithm: Union-Find (Disjoint Set Union – DSU)

    What Are We Solving?
    - You want to group plans or features that are transitively connected via shared qualifications or feature dependencies.
    - For example:
        - PlanA shares a feature with PlanB, and PlanB shares a feature with PlanC → so all 3 belong to the same cluster.

     */
    @Override
    public void clusterPlansUnionFind(DSAPatternReq req, DSAPatternResp resp) {

        List<Object[]> edges = repository.findAllProductFeatureEdges();

        if (edges.isEmpty()) {
            resp.setMessage("No feature dependencies found.");
            return;
        }

        // Step 1: Initialize Union-Find structure
        UnionFind uf = new UnionFind();

        // iterating over the list of edges fetched from the database.
        for (Object[] edge : edges) {
            // Each edge is a pair of values:
            // These represent a connection between a product and a feature.

            // This is the product ID (e.g., "EC2", "Lambda").
            String product = (String) edge[0];

            // This is the feature code (e.g., "Auto-scaling", "Subsecond billing").
            String feature = (String) edge[1];

            // This is the key step where we're saying:
            // This product and this feature are connected, so they should belong to the same cluster.
            /*
            The UnionFind data structure:
            - Maintains disjoint sets of connected elements.
            - Efficiently merges two sets using union().
            - Internally maps every item to a representative (root).
            - Uses path compression to keep things fast.

            Conceptual Analogy:
                Imagine products and features are dots,
                and every call to union(product, feature) is drawing a line connecting them.

                After processing all edges, connected components form naturally — that’s your cluster.
             */
            uf.union(product, feature); // Link product to feature
        }

        // After this loop, all related products and features are logically grouped. Later, we extract clusters like:

        // Step 2: Build clusters by root
        Map<String, List<String>> clusters = new HashMap<>();
        for (String node : uf.getAllNodes()) {
            String root = uf.find(node);
            clusters.computeIfAbsent(root, k -> new ArrayList<>()).add(node);
        }
        // This groups all nodes under their cluster representative (root).

        resp.setMessage("Clustered plans using Union-Find");
        resp.setResult(clusters);

        /*
        What The Output Represents
        - Each key in the JSON map is a representative root node of a cluster, typically the first node encountered in
        a group of interconnected elements (AWS services + features).

        - Each value list is a cluster — a group of related product features and services that are transitively
        connected, meaning there is at least one path between them via the union operations you performed.

        This structure is ideal for:
        - Identifying product modules or families
        - Analyzing eligibility clusters in healthcare
        - Detecting feature propagation logic

        How This Helps in Real Business Use Cases
        - For Healthcare Eligibility:
        -   Each cluster might represent all features a user gets access to once they qualify for a particular product or feature.
        -   You can track eligibility propagation in in-place operations or visualize feature graphs.

        - For AWS Platform Design:
        -   You now know what core features are tightly coupled.
        -   This can drive pricing packages, bundling strategies, or even microservices grouping.
         */
    }

    @Override
    public void computeUpgradePaths(DSAPatternReq req, DSAPatternResp resp) {
        String productId = req.getProductId();
        String start = req.getCurrentPlan(); // or req.getStartFeature()

        List<ProductFeatureDependency> edges = repository.findByProductId(productId);

        if (ObjectUtils.isEmpty(edges)) {
            resp.setMessage("No upgrade dependency data found for productId: " + productId);
            return;
        }

        Map<String, List<String>> adjList = new HashMap<>();
        for (ProductFeatureDependency edge : edges) {
            adjList.computeIfAbsent(edge.getSourceFeatureCode(), k -> new ArrayList<>())
                    .add(edge.getDependentFeatureCode());
        }

        int maxDepth = req.getMaxDepth() > 0 ? req.getMaxDepth() : Integer.MAX_VALUE;
        boolean excludeCycles = req.isExcludeCycles();

        List<List<String>> allPaths = new ArrayList<>();
        dfsAllUpgradePaths(start, adjList, new ArrayList<>(), allPaths, new HashSet<>(), excludeCycles, 0, maxDepth);

        resp.setMessage("Upgrade paths from " + start + (excludeCycles ? " (no cycles)" : "") +
                (maxDepth != Integer.MAX_VALUE ? ", maxDepth=" + maxDepth : ""));
        resp.setResult(allPaths);
    }

    @SuppressWarnings("SequencedCollectionMethodCanBeUsed")
    private void dfsAllUpgradePaths(
            String current,
            Map<String, List<String>> graph,
            List<String> path,
            List<List<String>> allPaths,
            Set<String> visited,
            boolean excludeCycles,
            int depth,
            int maxDepth) {

        if (depth > maxDepth) return;

        path.add(current);
        if (excludeCycles) visited.add(current);

        List<String> neighbors = graph.getOrDefault(current, Collections.emptyList());
        if (neighbors.isEmpty()) {
            allPaths.add(new ArrayList<>(path));
        } else {
            for (String neighbor : neighbors) {
                if (!excludeCycles || !visited.contains(neighbor)) {
                    dfsAllUpgradePaths(neighbor, graph, path, allPaths, visited, excludeCycles, depth + 1, maxDepth);
                }
            }
        }

        path.remove(path.size() - 1);
        if (excludeCycles) visited.remove(current);
    }



    @SuppressWarnings("SequencedCollectionMethodCanBeUsed")
    private void bfsAllUpgradePaths(
            String start,
            Map<String, List<String>> adjList,
            List<List<String>> allPaths
    ) {
        Queue<List<String>> queue = new ArrayDeque<>();
        queue.add(List.of(start));

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String last = path.get(path.size() - 1);

            List<String> neighbors = adjList.getOrDefault(last, List.of());

            if (neighbors.isEmpty()) {
                allPaths.add(path);
            } else {
                for (String neighbor : neighbors) {
                    if (!path.contains(neighbor)) { // Avoid cycles
                        List<String> newPath = new ArrayList<>(path);
                        newPath.add(neighbor);
                        queue.add(newPath);
                    }
                }
            }
        }
    }

}
