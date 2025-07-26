package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import java.math.BigDecimal;


public interface SlidingWindowPlanAnalyzer {

    BigDecimal getAccountPurchasesMaxCostInWindow(String accountId, int windowSizeInDays);

    BigDecimal getAccountPurchasesMaxCostInWindowWithJPAStreams(String accountNbr, int windowSizeInDays);
}
