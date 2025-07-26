package com.jk.solutions.data_structures.health_care.plans_mgmt.repository;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.ProductPriceProjection;
import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT new com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.ProductPriceProjection(CAST(p.productId AS string), p.priceAmount) FROM ProductPrice p WHERE priceAmount IS NOT NULL")
    List<ProductPriceProjection> findAllProductPrices();
}
