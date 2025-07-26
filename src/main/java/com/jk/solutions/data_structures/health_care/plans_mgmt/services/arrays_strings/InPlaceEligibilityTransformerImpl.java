package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternReq;
import com.jk.solutions.data_structures.health_care.plans_mgmt.dtos.DSAPatternResp;
import org.springframework.stereotype.Component;

import com.jk.solutions.data_structures.health_care.plans_mgmt.common.AlgorithmMethodType;
import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.AccountProductEligibility;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.AccountProductEligibilityRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class InPlaceEligibilityTransformerImpl implements InPlaceEligibilityTransformer{

    @Autowired
    private AccountProductEligibilityRepository eligibilityRepository;

    @Override
    @Transactional
    public void transformEligibilityInPlace(DSAPatternReq req, DSAPatternResp resp) {
        String accountNbr = req.getAccountNbr();
        AlgorithmMethodType methodType;

        try {
            methodType = AlgorithmMethodType.valueOf(req.getMethodType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid method type: " + req.getMethodType());
        }

        switch (methodType) {
            case STANDARD -> standardTransform(accountNbr, resp);
            case MEMORY_EFFICIENT -> memoryEfficientTransform(accountNbr, resp);
            case TIME_OPTIMIZED -> timeOptimizedTransform(accountNbr, resp);
            case DATABASE_OPTIMIZED -> databaseOptimizedTransform(accountNbr, resp);
        }
    }

    private void standardTransform(String accountNbr, DSAPatternResp resp) {
        List<AccountProductEligibility> eligibilities = eligibilityRepository.findByAccountNumber(accountNbr);

        if (ObjectUtils.isEmpty(eligibilities)) {
            resp.setMessage("No eligibility records found for account.");
            return;
        }

        for (AccountProductEligibility eligibility : eligibilities) {
            eligibility.setEligible(!eligibility.isEligible());
            eligibility.setEligibilityReason("Inverted in-place by STANDARD logic");
            eligibility.setLastEvaluatedDate(LocalDate.now());
            eligibility.setEligibilityVersion("v1.0");
        }

        resp.setMessage("Eligibility flags updated using standard in-place transformation.");
        resp.addResult("updatedCount", eligibilities.size());
    }

    private void memoryEfficientTransform(String accountNbr, DSAPatternResp resp) {
        List<Long> ids = eligibilityRepository.findIdsByAccountNumber(accountNbr);
        int updated = 0;

        for (Long id : ids) {
            eligibilityRepository.toggleEligibilityFlagById(id);
            updated++;
        }

        resp.setMessage("Eligibility flags updated using memory-efficient transformation.");
        resp.addResult("updatedCount", updated);
    }

    private void timeOptimizedTransform(String accountNbr, DSAPatternResp resp) {
        int updatedCount = eligibilityRepository.bulkToggleEligibilityAndUpdate(accountNbr, LocalDate.now(), "v2.0");

        resp.setMessage("Eligibility flags updated using time-optimized in-place transformation.");
        resp.addResult("updatedCount", updatedCount);
    }

    private void databaseOptimizedTransform(String accountNbr, DSAPatternResp resp) {
        int updatedCount = eligibilityRepository.dbSideEligibilityInversion(accountNbr, "v3.0");

        resp.setMessage("Eligibility flags updated using database-optimized transformation.");
        resp.addResult("updatedCount", updatedCount);
    }
}
