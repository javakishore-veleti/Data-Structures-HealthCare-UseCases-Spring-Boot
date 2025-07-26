package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.common.AlgorithmMethodType;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.ProductPriceProjection;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.ProductPriceRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.math.BigDecimal;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class TwoPointerProductAnalyzerImpl implements TwoPointerProductAnalyzer {

    @Autowired
    private ProductPriceRepository productPriceRepository;


    @Override
    public void checkEligibleProductBundleWithinBudget(DSAPatternReq req, DSAPatternResp resp) {
        List<ProductPriceProjection> products = productPriceRepository.findAllProductPrices();

        if (ObjectUtils.isEmpty(products)) {
            resp.setMessage("No products found.");
            return;
        }

        String methodTypeStr = req.getMethodType(); // This is your input string
        AlgorithmMethodType methodType;

        try {
            methodType = AlgorithmMethodType.valueOf(methodTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid algorithm method type: " + methodTypeStr);
        }

        /*
            What Two Pointers Is Actually Doing:

            Given a sorted array of product prices, you're trying to find pairs of products such that:
            productPrice[left] + productPrice[right] <= budget

            And you're:
                Starting with left = 0 (smallest price),
                And right = n - 1 (largest price),
                Then moving the pointers towards each other to explore combinations.

            You’re not dividing the array into two equal halves.
            Instead, you're exploring complementary values — cheap + expensive — to stay under or hit a budget.
         */
        switch (methodType) {
            case STANDARD -> {
                products.sort(Comparator.comparing(ProductPriceProjection::getPriceAmount));
                standardTwoPointer(products, req.getBudget(), resp);
            }
            case MEMORY_EFFICIENT -> {
                products.sort(Comparator.comparing(ProductPriceProjection::getPriceAmount));
                memoryEfficientTwoPointer(products, req.getBudget(), resp);
            }
            case TIME_OPTIMIZED -> {
                products.sort(Comparator.comparing(ProductPriceProjection::getPriceAmount));
                timeOptimizedTwoPointer(products, req.getBudget(), resp);
            }
            case DATABASE_OPTIMIZED -> dbOptimizedStub(products, req.getBudget(), resp); // Placeholder
        }
    }

    private void standardTwoPointer(List<ProductPriceProjection> products, BigDecimal budget, DSAPatternResp resp) {
        List<List<String>> eligibleCombos = new ArrayList<>();
        int left = 0, right = products.size() - 1;

        while (left < right) {
            BigDecimal total = products.get(left).getPriceAmount().add(products.get(right).getPriceAmount());

            if (total.compareTo(budget) <= 0) {
                eligibleCombos.add(Arrays.asList(products.get(left).getProductId(), products.get(right).getProductId()));
                left++; // Try next higher priced left product
            } else {
                right--; // Total too high, move right pointer left
            }
        }
        resp.setResult(eligibleCombos);
        resp.setMessage("Found " + eligibleCombos.size() + " eligible product bundles.");
    }

    private void memoryEfficientTwoPointer(List<ProductPriceProjection> products, BigDecimal budget, DSAPatternResp resp) {
        List<String> summary = new ArrayList<>();
        int left = 0, right = products.size() - 1;

        while (left < right) {
            BigDecimal total = products.get(left).getPriceAmount().add(products.get(right).getPriceAmount());

            if (total.compareTo(budget) <= 0) {
                summary.add("Product [" + products.get(left).getProductId() + "] + [" + products.get(right).getProductId() + "]");
                left++;
            } else {
                right--;
            }
        }
        resp.setMessage("Memory efficient summary: Found " + summary.size() + " bundles");
        resp.setResult(summary);
    }

    private void timeOptimizedTwoPointer(List<ProductPriceProjection> products, BigDecimal budget, DSAPatternResp resp) {
        Set<String> fastMatchCombos = new HashSet<>();
        Map<BigDecimal, String> priceToProduct = new HashMap<>();

        for (ProductPriceProjection p : products) {
            BigDecimal complement = budget.subtract(p.getPriceAmount());
            if (priceToProduct.containsKey(complement)) {
                fastMatchCombos.add("[" + p.getProductId() + "," + priceToProduct.get(complement) + "]");
            }
            priceToProduct.put(p.getPriceAmount(), p.getProductId());
        }

        resp.setMessage("Time-optimized result: " + fastMatchCombos.size() + " found");
        resp.setResult(fastMatchCombos);
    }

    @SuppressWarnings("unused")
    private void dbOptimizedStub(List<ProductPriceProjection> products, BigDecimal budget, DSAPatternResp resp) {
        resp.setMessage("Use a database-native window function or stored procedure here (not implemented).");
        resp.setResult(Collections.emptyList());
    }

}
