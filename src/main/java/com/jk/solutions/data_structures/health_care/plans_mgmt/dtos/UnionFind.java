package com.jk.solutions.data_structures.health_care.plans_mgmt.dtos;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class UnionFind {

    private final Map<String, String> parent = new HashMap<>();

    public void union(String a, String b) {
        String rootA = find(a);
        String rootB = find(b);
        if (!rootA.equals(rootB)) {
            parent.put(rootA, rootB);
        }
    }

    public String find(String node) {
        parent.putIfAbsent(node, node);
        if (!node.equals(parent.get(node))) {
            parent.put(node, find(parent.get(node))); // Path compression
        }
        return parent.get(node);
    }

    public Set<String> getAllNodes() {
        return parent.keySet();
    }
}
