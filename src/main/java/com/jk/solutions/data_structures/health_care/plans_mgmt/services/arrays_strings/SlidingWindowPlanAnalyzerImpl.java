package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.AccountPlanOrderRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
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

    @Autowired
    private AccountPlanOrderRepository accountPlanOrderRepository;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public BigDecimal getAccountPurchasesMaxCostInWindow(String accountNbr, int windowSizeInDays) {
        List<BigDecimal> acctOrdersTotals = accountPlanOrderRepository.findOrderTotalCostsByAccountNumberOOrderByCreatedAt(accountNbr);

        if(ObjectUtils.isEmpty(acctOrdersTotals) || acctOrdersTotals.size() < windowSizeInDays){
            return BigDecimal.ZERO;
        }

        // Initial window sum
        BigDecimal currentWindowSum = BigDecimal.ZERO;
        // start from Day 1
        for(int dayIndex = 0; dayIndex < windowSizeInDays; dayIndex++) {
            currentWindowSum = currentWindowSum.add(acctOrdersTotals.get(dayIndex));
        }

        // move to next day i.e. Day 2
        BigDecimal maxWindowSum = currentWindowSum;

        for(int dayIndex = windowSizeInDays; dayIndex < acctOrdersTotals.size(); dayIndex++) {

            currentWindowSum = currentWindowSum.add(acctOrdersTotals.get(dayIndex)).subtract(acctOrdersTotals.get(dayIndex - windowSizeInDays));
            maxWindowSum = maxWindowSum.max(currentWindowSum);

        }
        return maxWindowSum;
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getAccountPurchasesMaxCostInWindowWithJPAStreams(String accountNbr, int windowSizeInDays) {
        BigDecimal maxSum = BigDecimal.ZERO;

        try (Stream<BigDecimal> costStream = accountPlanOrderRepository
                .streamOrderTotalCostsByAccountNumberOrderByCreatedAt(accountNbr)) {

            Deque<BigDecimal> window = new ArrayDeque<>(windowSizeInDays);
            BigDecimal currentSum = BigDecimal.ZERO;

            Iterator<BigDecimal> iterator = costStream.iterator();
            while (iterator.hasNext()) {
                BigDecimal cost = iterator.next();
                window.addLast(cost);
                currentSum = currentSum.add(cost);

                if (window.size() > windowSizeInDays) {
                    BigDecimal removed = window.removeFirst();
                    currentSum = currentSum.subtract(removed);
                }

                // Update max sum when window is valid
                if (window.size() == windowSizeInDays) {
                    maxSum = maxSum.max(currentSum);
                }
            }
        }

        return maxSum;
    }
}
