package com.jk.solutions.data_structures.health_care.plans_mgmt.services.arrays_strings;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.AccountProductEligibility;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.AccountProductEligibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
public class AccountProductEligibilityDataSeederImpl implements AccountProductEligibilityDataSeeder {

    @Autowired
    private AccountProductEligibilityRepository repository;

    private static final String[] PLAN_TIERS = {"BRONZE", "SILVER", "GOLD", "PLATINUM"};
    private static final String[] CHANNELS = {"ONLINE", "AGENT", "CALL_CENTER"};
    private static final String[] COUNTRIES = {"US", "IN", "CA"};
    private static final String[] COUNTIES = {"001", "002", "003"};
    private static final String[] FEATURES = {"F1", "F2", "F3"};

    @Override
    public String generateSyntheticEligibilityData(String accountNumber, int count) {
        Random random = new Random();

        IntStream.range(0, count).forEach(i -> {
            AccountProductEligibility entity = new AccountProductEligibility();
            entity.setId(UUID.randomUUID().toString());
            entity.setAccountNumber(accountNumber);
            entity.setProductId("PROD-" + UUID.randomUUID());
            entity.setProductFeatureCode(FEATURES[random.nextInt(FEATURES.length)]);
            entity.setPlanTier(PLAN_TIERS[random.nextInt(PLAN_TIERS.length)]);
            entity.setChannelType(CHANNELS[random.nextInt(CHANNELS.length)]);
            entity.setCountryCode(COUNTRIES[random.nextInt(COUNTRIES.length)]);
            entity.setCountyCode(COUNTIES[random.nextInt(COUNTIES.length)]);
            entity.setEligible(random.nextBoolean());
            entity.setEligibilityReason("AUTO-GEN");
            entity.setLastEvaluatedDate(LocalDate.now());
            entity.setEligibilityVersion("v1.0");

            repository.save(entity);
        });

        return "Seeded " + count + " account-product eligibility records for account: " + accountNumber;
    }
}
