package com.jk.solutions.data_structures.health_care.plans_mgmt.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductPriceProjection {
    private String productId;
    private BigDecimal priceAmount;

    public ProductPriceProjection(String productId, BigDecimal priceAmount) {
        this.productId = productId;
        this.priceAmount = priceAmount;
    }

}
