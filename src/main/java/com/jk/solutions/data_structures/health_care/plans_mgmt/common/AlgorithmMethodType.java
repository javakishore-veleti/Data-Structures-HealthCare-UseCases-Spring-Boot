package com.jk.solutions.data_structures.health_care.plans_mgmt.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AlgorithmMethodType {
    STANDARD,
    MEMORY_EFFICIENT,
    TIME_OPTIMIZED,
    DATABASE_OPTIMIZED;

    @JsonValue
    public String toValue() {
        return this.name();
    }

    @JsonCreator
    public static AlgorithmMethodType fromValue(String value) {
        return AlgorithmMethodType.valueOf(value.toUpperCase());
    }
}
