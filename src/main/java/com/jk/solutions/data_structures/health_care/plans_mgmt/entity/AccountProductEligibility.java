package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "account_product_eligibility")
public class AccountProductEligibility extends BaseEntity {

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_feature_code")
    private String productFeatureCode;

    @Column(name = "plan_tier")
    private String planTier;

    @Column(name = "channel_type")
    private String channelType;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "county_code")
    private String countyCode;

    @Column(name = "is_eligible", nullable = false)
    private boolean isEligible;

    @Column(name = "eligibility_reason")
    private String eligibilityReason;

    @Column(name = "last_evaluated_date")
    private LocalDate lastEvaluatedDate;

    @Column(name = "eligibility_version")
    private String eligibilityVersion;
}