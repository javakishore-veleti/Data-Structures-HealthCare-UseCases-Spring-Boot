package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_feature_dependency")
@Getter
@Setter
@NoArgsConstructor
public class ProductFeatureDependency extends BaseEntity {

    @Column(name = "source_feature_code", nullable = false)
    private String sourceFeatureCode;

    @Column(name = "dependent_feature_code", nullable = false)
    private String dependentFeatureCode;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "dependency_weight")
    private Integer dependencyWeight; // e.g., 1 to 100 (priority, effort, etc.)

    public ProductFeatureDependency(String productId, String fromFeature, String toFeature, Integer dependencyWeight) {
        this.productId = productId;
        this.sourceFeatureCode = fromFeature;
        this.dependentFeatureCode = toFeature;
        this.dependencyWeight = dependencyWeight;
    }
}