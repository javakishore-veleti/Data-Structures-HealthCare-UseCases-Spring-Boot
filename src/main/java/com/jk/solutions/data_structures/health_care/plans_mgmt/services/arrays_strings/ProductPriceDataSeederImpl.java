package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.ProductPrice;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.ProductPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class ProductPriceDataSeederImpl implements ProductPriceDataSeeder {

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Override
    public String populateSyntheticProductPrices(int count) {
        List<ProductPrice> prices = new ArrayList<>(count);
        Random random = new Random();

        for (int i = 1; i <= count; i++) {
            ProductPrice price = new ProductPrice();
            price.setId(UUID.randomUUID().toString());
            price.setProductId(price.getId());
            price.setPriceAmount(BigDecimal.valueOf(50 + (5000 - 50) * random.nextDouble()).setScale(2, RoundingMode.HALF_UP));
            price.setCurrency("USD");
            price.setStatus("ACTIVE");
            price.setVersion(1);
            price.setCreatedAt(LocalDateTime.now());
            price.setCreatedBy("seeder");
            price.setUpdatedAt(LocalDateTime.now());
            price.setUpdatedBy("seeder");
            price.setIsDeleted(false);
            prices.add(price);
        }

        productPriceRepository.saveAll(prices);
        return "Inserted " + count + " product price records.";
    }
}
