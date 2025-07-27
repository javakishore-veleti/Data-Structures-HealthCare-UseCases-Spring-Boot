package com.jk.solutions.data_structures.health_care.plans_mgmt.repository;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.ProductFeatureDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ProductFeatureDependencyRepository extends JpaRepository<ProductFeatureDependency, String> {
    List<ProductFeatureDependency> findByProductId(String productId);

    @Query("SELECT p FROM ProductFeatureDependency p WHERE p.productId = :productId")
    Stream<ProductFeatureDependency> streamDependenciesByProductId(@Param("productId") String productId);

    @SuppressWarnings("SqlResolve")
    @Query(value = """
    WITH RECURSIVE feature_order(source, target, depth) AS (
      SELECT source_feature_code, dependent_feature_code, 1
      FROM dsa_healthcare_db.product_feature_dependency
      WHERE product_id = :productId

      UNION ALL

      SELECT fo.source, pfd.dependent_feature_code, fo.depth + 1
      FROM dsa_healthcare_db.feature_order fo
      JOIN dsa_healthcare_db.product_feature_dependency pfd ON fo.target = pfd.source_feature_code
      WHERE pfd.product_id = :productId
    )
    SELECT DISTINCT target FROM feature_order ORDER BY depth
    """, nativeQuery = true)
    List<String> findTopologicallySortedFeatures(@Param("productId") String productId);

    @SuppressWarnings("SqlResolve")
    @Query(value = """
    WITH RECURSIVE feature_order(source, target, depth) AS (
      SELECT source_feature_code, dependent_feature_code, 1
      FROM dsa_healthcare_db.product_feature_dependency
      WHERE product_id = :productId

      UNION ALL

      SELECT fo.source, pfd.dependent_feature_code, fo.depth + 1
      FROM dsa_healthcare_db.feature_order fo
      JOIN dsa_healthcare_db.product_feature_dependency pfd ON fo.target = pfd.source_feature_code
      WHERE pfd.product_id = :productId
    )
    SELECT DISTINCT target, depth FROM feature_order ORDER BY depth
    """, nativeQuery = true)
    List<Object[]> findTopologicallySortedFeaturesWithDepth(@Param("productId") String productId);

    @Query("SELECT pfd.productId, pfd.dependentFeatureCode FROM ProductFeatureDependency pfd")
    List<Object[]> findAllProductFeatureEdges();

}
