package com.jk.solutions.data_structures.health_care.plans_mgmt.repository;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.ProductFeatureDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFeatureDependencyRepository extends JpaRepository<ProductFeatureDependency, String> {
    List<ProductFeatureDependency> findByProductId(String productId);
}
