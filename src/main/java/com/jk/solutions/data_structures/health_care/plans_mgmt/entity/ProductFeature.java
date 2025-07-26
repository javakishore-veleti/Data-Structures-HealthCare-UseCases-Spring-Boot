package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ProductFeature")
public class ProductFeature extends BaseEntity {

    @Column(name = "feature_id", nullable = false)
    private Long featureId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "feature_name", length = 255)
    private String featureName;

    @Column(name = "feature_description", length = 1000)
    private String featureDescription;
}
