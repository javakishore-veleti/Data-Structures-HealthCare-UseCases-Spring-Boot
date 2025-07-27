package com.jk.solutions.data_structures.health_care.plans_mgmt.api;

import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.AccountPlanOrderDataSeeder;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.AccountProductEligibilityDataSeeder;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.ProductPriceDataSeeder;
import com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs.FeatureDependencyDataSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dsa/seed")
public class DataSeederController {

    @Autowired
    private ProductPriceDataSeeder productPriceDataSeeder;

    @Autowired
    private AccountPlanOrderDataSeeder accountPlanOrderDataSeeder;

    @Autowired
    private AccountProductEligibilityDataSeeder eligibilityDataSeeder;

    @Autowired
    private FeatureDependencyDataSeeder featureDependencyDataSeeder;


    @PostMapping("/arrays-strings/product-prices")
    public ResponseEntity<String> seedProductPrices(@RequestParam(defaultValue = "1000") int count) {
        String result = productPriceDataSeeder.populateSyntheticProductPrices(count);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/arrays-strings/account-plan-orders")
    public ResponseEntity<String> seedAccountPlanOrders(
            @RequestParam String accountNbr,
            @RequestParam(defaultValue = "1000") int count) {
        String msg = accountPlanOrderDataSeeder.populateSyntheticAccountPlanOrders(accountNbr, count);
        return ResponseEntity.ok(msg);
    }

    /**
     * Generate synthetic account-product eligibility data.
     * Example: /api/dsa/seed/account-product-eligibility?account=ACC123&count=100
     */
    @PostMapping("/arrays-strings/account-product-eligibility")
    public ResponseEntity<String> seedAccountProductEligibility(
            @RequestParam(name = "account", defaultValue = "ACC123") String accountNumber,
            @RequestParam(name = "count", defaultValue = "100") int count) {

        String result = eligibilityDataSeeder.generateSyntheticEligibilityData(accountNumber, count);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/graphs/feature-dependencies")
    public ResponseEntity<String> seedGraphDependencies(
            @RequestParam(defaultValue = "100") int numProducts,
            @RequestParam(defaultValue = "20") int avgEdgesPerProduct) {
        String result = featureDependencyDataSeeder.populateFeatureDependencies(numProducts, avgEdgesPerProduct);
        return ResponseEntity.ok(result);
    }


}
