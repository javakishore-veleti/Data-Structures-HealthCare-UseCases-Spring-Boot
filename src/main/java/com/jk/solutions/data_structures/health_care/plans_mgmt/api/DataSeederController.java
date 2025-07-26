package com.jk.solutions.data_structures.health_care.plans_mgmt.api;

import com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings.ProductPriceDataSeeder;
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

    @PostMapping("/product-prices")
    public ResponseEntity<String> seedProductPrices(@RequestParam(defaultValue = "1000") int count) {
        String result = productPriceDataSeeder.populateSyntheticProductPrices(count);
        return ResponseEntity.ok(result);
    }
}
