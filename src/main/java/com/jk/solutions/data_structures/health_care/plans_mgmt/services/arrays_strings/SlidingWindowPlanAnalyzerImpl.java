package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.AccountPlanOrder;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
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
        LOGGER.info("Elapsed time in ms: {}" , (elapsed / 1_000_000));

        resp.addResult("maxSum", maxSum);
        resp.setExecutionTimeMs(elapsed);
    }

    @Override
    @Transactional
    public void populateSyntheticOrders(String accountNbr, int days, BigDecimal baseAmount) {
        List<AccountPlanOrder> orders = new ArrayList<>();
        LocalDate today = LocalDate.now();
        Random random = new Random();

        for (int i = 0; i < days; i++) {
            // Randomize base amount ±20% variance
            BigDecimal randomAmount = baseAmount
                    .multiply(BigDecimal.valueOf(0.8 + (0.4 * random.nextDouble())))
                    .setScale(2, RoundingMode.HALF_UP);

            AccountPlanOrder order = new AccountPlanOrder();
            order.setId(UUID.randomUUID().toString());
            order.setOrderId(order.getId());
            order.setAccountNumber(accountNbr);
            order.setPlanId(UUID.randomUUID().toString());
            order.setOrderLinesCount(1);
            order.setOrderCost(randomAmount);
            order.setOrderDiscount(BigDecimal.ZERO);
            order.setOrderTotalCost(randomAmount);
            order.setOrderStatus("COMPLETED");
            order.setOrderFulfilledDate(today.minusDays(i));
            order.setCreatedAt(today.minusDays(i).atStartOfDay());
            order.setCreatedBy("SYNTHETIC");
            order.setUpdatedAt(today.minusDays(i).atStartOfDay());
            order.setUpdatedBy("SYNTHETIC");
            order.setIsDeleted(false);

            orders.add(order);
        }

        accountPlanOrderRepository.saveAll(orders);
    }
}
