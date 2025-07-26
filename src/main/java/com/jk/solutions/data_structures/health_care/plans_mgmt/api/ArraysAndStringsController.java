package com.jk.solutions.data_structures.health_care.plans_mgmt.api;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.InPlaceEligibilityTransformer;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.PrefixSumPlanAnalyzer;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.SlidingWindowPlanAnalyzer;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.TwoPointerProductAnalyzer;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RequestMapping("/api/dsa/arrays-strings")
@RestController
public class ArraysAndStringsController {

    @Autowired
    private SlidingWindowPlanAnalyzer slidingWindowPlanAnalyzer;

    @Autowired
    private TwoPointerProductAnalyzer twoPointerProductAnalyzer;

    @Autowired
    private PrefixSumPlanAnalyzer prefixSumPlanAnalyzer;

    @Autowired
    private InPlaceEligibilityTransformer inPlaceEligibilityTransformer;

    /**
     * Data Structure Algorithm: Arrays and Strings
     * Pattern: Sliding Window
     * Use Case: Analyze  purchase cost in moving time windows.
     * Endpoint to get maximum plan purchase cost in a sliding window for an account.
     * Example: /api/dsa/arrays-strings/sliding-window?accountNbr=ACC123&windowSize=7
     *
     *
     */
    @GetMapping("/sliding-window")
    public ResponseEntity<DSAPatternResp> getMaxSlidingWindowTotal(
            @RequestParam("accountNbr") String accountNbr,
            @RequestParam("windowSize") int windowSize) {

        DSAPatternReq req = DSAPatternReq.builder()
                .accountNbr(accountNbr)
                .windowSize(windowSize)
                .build();

        DSAPatternResp response = new DSAPatternResp();
        response.setDataStructureAlgorithmName("Arrays-Strings");
        response.setPatternName("Sliding-Window");
        slidingWindowPlanAnalyzer.getAccountPurchasesMaxCostInWindowWithJPAStreams(req, response);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to populate synthetic orders for testing.
     * Example: /api/dsa/arrays-strings/sliding-window/populate?accountNbr=ACC123&days=30&amount=100
     */
    @GetMapping("/sliding-window/populate")
    public ResponseEntity<String> populateSlidingWindowData(
            @RequestParam("accountNbr") String accountNbr,
            @RequestParam("days") int days,
            @RequestParam("amount") BigDecimal baseAmount) {

        slidingWindowPlanAnalyzer.populateSyntheticOrders(accountNbr, days, baseAmount);
        return ResponseEntity.ok("Inserted " + days + " synthetic orders for account: " + accountNbr);
    }

    /*
        Two Pointers (a.k.a. Two Indexes)
        Use Case: Identify all product price pairs that sum within a user's budget.

        Key Features:
            Four algorithmic variants:
                STANDARD
                MEMORY_EFFICIENT
                TIME_OPTIMIZED
                DATABASE_OPTIMIZED

        - Input via DSAPatternReq; output via DSAPatternResp.
        - Custom ProductPriceProjection DTO for DB efficiency.
        - Supports 50k+ product scale with defensive null checks.
        - API endpoint: /api/dsa/arrays-strings/two-pointers/checkEligibleProductBundle
        - Enum-based switching using AlgorithmMethodType.
     */

    /**
     * Endpoint for Two Pointer pattern on ProductPrice.
     * Example: /api/dsa/arrays-strings/two-pointers?targetSum=120
     */
    @GetMapping("/two-pointers/checkEligibleProductBundle")
    public ResponseEntity<DSAPatternResp> checkEligibleProductBundle(
            @RequestParam("targetSum") double budget,
            @RequestParam(name = "method", required = false, defaultValue = "memory") String methodType) {

        DSAPatternReq req = DSAPatternReq.builder()
                .methodType(methodType)
                .budget(BigDecimal.valueOf(budget))
                .build();

        DSAPatternResp resp = new DSAPatternResp();
        twoPointerProductAnalyzer.checkEligibleProductBundleWithinBudget(req, resp);

        return ResponseEntity.ok(resp);
    }

    @Timed(value = "prefix.sum.analysis", histogram = true)
    @GetMapping("/prefix-sum/range-total")
    public ResponseEntity<DSAPatternResp> analyzeCumulativeRangeTotal(
            @RequestParam("accountNbr") String accountNbr,
            @RequestParam("indexFrom") int indexFrom,
            @RequestParam("indexTo") int indexTo,
            @RequestParam(name = "methodType", defaultValue = "standard") String methodType) {

        DSAPatternReq req = DSAPatternReq.builder()
                .accountNbr(accountNbr)
                .indexFrom(indexFrom)
                .indexTo(indexTo)
                .methodType(methodType)
                .build();

        DSAPatternResp resp = new DSAPatternResp();
        prefixSumPlanAnalyzer.analyzeCumulativeTotalCostRange(req, resp);

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/in-place/update-eligibility")
    public ResponseEntity<DSAPatternResp> updateEligibilityInPlace(
            @RequestParam("accountNbr") String accountNbr,
            @RequestParam(name = "methodType", defaultValue = "standard") String methodType) {

        DSAPatternReq req = DSAPatternReq.builder()
                .accountNbr(accountNbr).methodType(methodType)
                .build();

        DSAPatternResp resp = new DSAPatternResp();
        inPlaceEligibilityTransformer.transformEligibilityInPlace(req, resp);
        return ResponseEntity.ok(resp);
    }
}
