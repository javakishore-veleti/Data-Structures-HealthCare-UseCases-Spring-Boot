package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.AccountPlanOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SlidingWindowPlanAnalyzerImplTest {

    private AccountPlanOrderRepository accountPlanOrderRepository;
    private SlidingWindowPlanAnalyzerImpl slidingWindowPlanAnalyzer;

    @BeforeEach
    public void setup() {
        accountPlanOrderRepository = mock(AccountPlanOrderRepository.class);
        slidingWindowPlanAnalyzer = new SlidingWindowPlanAnalyzerImpl();
        slidingWindowPlanAnalyzer.setAccountPlanOrderRepository(accountPlanOrderRepository);
    }

    @Test
    void getAccountPurchasesMaxCostInWindow() {

        String accountNumber = "ACC-123";
        int windowSize = 3;

        // Example cost per day: [100, 200, 150, 50, 400]
        List<BigDecimal> costs = List.of(
                new BigDecimal("100"),
                new BigDecimal("200"),
                new BigDecimal("150"),
                new BigDecimal("50"),
                new BigDecimal("400")
        );

        // Mock the stream method
        when(accountPlanOrderRepository
                .streamOrderTotalCostsByAccountNumberOrderByCreatedAt(accountNumber))
                .thenReturn(costs.stream());

        DSAPatternReq req = DSAPatternReq.builder()
                .accountNbr(accountNumber)
                .windowSize(windowSize)
                .build();

        DSAPatternResp response = new DSAPatternResp();
        slidingWindowPlanAnalyzer.getAccountPurchasesMaxCostInWindowWithJPAStreams(req, response);

        // Expected max window: 200 + 150 + 50 = 400, or 150 + 50 + 400 = 600
        assertEquals(new BigDecimal("600"), response.getResults().get("maxSum"));
    }

    @Test
    void getAccountPurchasesMaxCostInWindowWithJPAStreams() {
        String accountNumber = "ACC-456";
        int windowSize = 5;

        List<BigDecimal> costs = List.of(
                new BigDecimal("100"),
                new BigDecimal("200")
        );

        when(accountPlanOrderRepository
                .streamOrderTotalCostsByAccountNumberOrderByCreatedAt(accountNumber))
                .thenReturn(costs.stream());

        DSAPatternReq req = DSAPatternReq.builder()
                .accountNbr(accountNumber)
                .windowSize(windowSize)
                .build();

        DSAPatternResp response = new DSAPatternResp();
        slidingWindowPlanAnalyzer.getAccountPurchasesMaxCostInWindowWithJPAStreams(req, response);

        assertEquals(BigDecimal.ZERO, response.getResults().get("maxSum"));
    }
}