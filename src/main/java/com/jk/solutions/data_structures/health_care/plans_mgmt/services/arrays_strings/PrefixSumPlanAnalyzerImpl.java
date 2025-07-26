package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.common.AlgorithmMethodType;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.AccountPlanOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
Purpose of the Class:
    PrefixSumPlanAnalyzerImpl implements the logic for analyzing cumulative total cost between two indices for a given account’s plan orders, based on different algorithm optimization goals:
        Standard
        Memory Efficient
        Time Optimized
        Database Optimized

    It uses the Prefix Sum pattern to calculate range sums efficiently on arrays derived from JPA-retrieved total costs (orderTotalCost).

    Performance Considerations:
        | Strategy         | Time Complexity              | Space Complexity | Best Use Case                               |
        | ---------------- | ---------------------------- | ---------------- | ------------------------------------------- |
        | Standard         | O(n) preprocess + O(1) query | O(n)             | Frequent range queries on static data       |
        | Memory Efficient | O(n) query                   | O(1)             | One-time use, small ranges                  |
        | Time Optimized   | Same as standard             | Same as standard | Optimized for speed (may use caching later) |
        | DB Optimized     | O(n) query via pagination    | Offloaded to DB  | Large data; database does the heavy lifting |

 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class PrefixSumPlanAnalyzerImpl implements PrefixSumPlanAnalyzer {

    @Autowired
    private AccountPlanOrderRepository accountPlanOrderRepository;

    @Override
    public void analyzeCumulativeTotalCostRange(DSAPatternReq req, DSAPatternResp resp) {

        String methodTypeStr = req.getMethodType();
        AlgorithmMethodType methodType;

        try {
            methodType = AlgorithmMethodType.valueOf(methodTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid method type: " + methodTypeStr);
        }

        switch (methodType) {
            case STANDARD -> {
                List<BigDecimal> orderCosts = accountPlanOrderRepository.findOrderTotalCostsByAccountNumberOOrderByCreatedAt(req.getAccountNbr());
                standardPrefixSum(orderCosts, req, resp);
            }
            case MEMORY_EFFICIENT -> {
                List<BigDecimal> orderCosts = accountPlanOrderRepository.findOrderTotalCostsByAccountNumberOOrderByCreatedAt(req.getAccountNbr());
                memoryEfficientPrefixSum(orderCosts, req, resp);
            }
            case TIME_OPTIMIZED -> {
                List<BigDecimal> orderCosts = accountPlanOrderRepository.findOrderTotalCostsByAccountNumberOOrderByCreatedAt(req.getAccountNbr());
                timeOptimizedPrefixSum(orderCosts, req, resp);
            }
            case DATABASE_OPTIMIZED -> dbOptimizedStub(req, resp);
        }
    }

    /*
    Logic: Precomputes prefix sum in a single pass.
    For any range [i..j], computes:

    Strength:
        O(1) query time for repeated requests after preprocessing.
        Best suited when multiple lookups are needed.
     */
    private void standardPrefixSum(List<BigDecimal> orderCosts, DSAPatternReq req, DSAPatternResp resp) {
        List<BigDecimal> prefixSum = new ArrayList<>();

        BigDecimal runningTotal = BigDecimal.ZERO;
        for(BigDecimal orderCost : orderCosts) {
            runningTotal = runningTotal.add(orderCost);
            prefixSum.add(runningTotal);
        }

        int indexFrom = req.getIndexFrom();
        int indexTo = req.getIndexTo();

        BigDecimal rangeTotal = prefixSum.get(indexTo).subtract(indexFrom > 0 ? prefixSum.get(indexFrom - 1) : BigDecimal.ZERO);
        resp.setMessage("Computed cumulative total cost using standardPrefixSum");
        resp.addResult("rangeTotal", rangeTotal);
    }

    /*
    Logic: On-the-fly summation only for the desired range [i..j].
    Strength:
        Minimal memory usage.
        Ideal when only one request needs to be answered.

     */
    private void memoryEfficientPrefixSum(List<BigDecimal> orderCosts, DSAPatternReq req, DSAPatternResp resp) {
        BigDecimal sum = BigDecimal.ZERO;
        for(int i = req.getIndexFrom(); i < req.getIndexTo(); i++) {
            sum = sum.add(orderCosts.get(i));
        }

        resp.setMessage("Computed cumulative total cost using memoryEfficientPrefixSum");
        resp.addResult("rangeTotal", sum);
    }

    /*
    Logic: Internally calls standardPrefixSum.
    Strength:
        Same as standard, but conceptually separated to allow future enhancements (like caching prefix sums).
     */
    private void timeOptimizedPrefixSum(List<BigDecimal> orderCosts, DSAPatternReq req, DSAPatternResp resp) {
        // Just delegating to standardPrefixSum for this scenario (same time efficiency)
        standardPrefixSum(orderCosts, req, resp);
        resp.setMessage("Computed using timeOptimizedPrefixSum)");
    }

    /*
    Logic: Uses Spring Data JPA PageRequest to simulate:
    Strength:
        Offloads work to the database to reduce in-memory usage.
        Particularly good when the dataset is large and network/memory pressure must be minimized.
     */
    private void dbOptimizedStub(DSAPatternReq req, DSAPatternResp resp) {

        int startIndex = req.getIndexFrom();
        int endIndex = req.getIndexTo();
        int limit = endIndex - startIndex + 1;

        PageRequest pageRequest = PageRequest.of(startIndex, limit, Sort.by("createdAt"));

        List<BigDecimal> windowedCosts = accountPlanOrderRepository
                .findWindowedOrderTotals(req.getAccountNbr(), pageRequest);

        BigDecimal result = windowedCosts.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        resp.setResult(result);
        resp.setMessage("Sum computed using JPA pagination (non-native) from index " + startIndex + " to " + endIndex);
    }
}
