package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.AccountPlanOrderRepository;
import io.micrometer.core.annotation.Timed;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Setter
@Getter
@Component
public class SlidingWindowPlanAnalyzerImpl implements SlidingWindowPlanAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlidingWindowPlanAnalyzerImpl.class);

    @Autowired
    private AccountPlanOrderRepository accountPlanOrderRepository;

    @Timed(value = "sliding.window.calculation", description = "Time spent analyzing sliding window cost", histogram = true)
    @Transactional(readOnly = true)
    @Override
    public void getAccountPurchasesMaxCostInWindowWithJPAStreams(DSAPatternReq req, DSAPatternResp resp) {
        long start = System.nanoTime();

        BigDecimal maxSum = BigDecimal.ZERO;

        try (Stream<BigDecimal> costStream = accountPlanOrderRepository
                .streamOrderTotalCostsByAccountNumberOrderByCreatedAt(req.getAccountNbr())) {

            Deque<BigDecimal> window = new ArrayDeque<>(req.getWindowSize());
            BigDecimal currentSum = BigDecimal.ZERO;

            Iterator<BigDecimal> iterator = costStream.iterator();
            while (iterator.hasNext()) {
                BigDecimal cost = iterator.next();
                window.addLast(cost);
                currentSum = currentSum.add(cost);

                if (window.size() > req.getWindowSize()) {
                    BigDecimal removed = window.removeFirst();
                    currentSum = currentSum.subtract(removed);
                }

                // Update max sum when window is valid
                if (window.size() == req.getWindowSize()) {
                    maxSum = maxSum.max(currentSum);
                }
            }
        }

        long elapsed = System.nanoTime() - start;
        LOGGER.info("Elapsed time in ms: " + (elapsed / 1_000_000));

        resp.addResult("maxSum", maxSum);
    }
}
