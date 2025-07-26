package com.jk.solutions.data_structures.health_care.plans_mgmt.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DSAPatternResp {

    private String dataStructureAlgorithmName;
    private String patternName;
    private Map<String, Object> inputParameters;
    private Object result;
    private long executionTimeMs;
    private String message;

    private Map<String, Object> results;

    public void addResult(String key, Object value) {
        if (results == null) {
            results = new LinkedHashMap<>();
        }
        results.put(key, value);
    }


}
