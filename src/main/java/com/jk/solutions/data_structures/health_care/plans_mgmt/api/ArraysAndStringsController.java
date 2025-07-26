package com.jk.solutions.data_structures.health_care.plans_mgmt.api;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.SlidingWindowPlanAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequestMapping("/api/dsa/arrays-strings")
@RestController
public class ArraysAndStringsController {

    @Autowired
    private SlidingWindowPlanAnalyzer slidingWindowPlanAnalyzer;

    /**
     * Endpoint to get maximum plan purchase cost in a sliding window for an account.
     * Example: /api/dsa/arrays-strings/sliding-window?accountNbr=ACC123&windowSize=7
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
}
