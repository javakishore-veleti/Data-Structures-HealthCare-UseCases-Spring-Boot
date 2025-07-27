package com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.ProductFeatureDependency;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.ProductFeatureDependencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FeatureDependencyDataSeederImpl implements FeatureDependencyDataSeeder{

    @Autowired
    private ProductFeatureDependencyRepository repository;

    @Override
    public String populateFeatureDependencies(int numProducts, int avgEdgesPerProduct) {
        List<String> awsFeatures = List.of(
                "IAM", "EC2", "S3", "Athena", "ECS", "Fargate", "EKS", "QuickSight",
                "CloudWatch", "VPC", "RDS", "Lambda", "DynamoDB", "SNS", "SQS", "StepFunctions"
        );

        List<ProductFeatureDependency> allDeps = new ArrayList<>();

        for (int i = 1; i <= numProducts; i++) {
            String productId = "AWS_PROD_" + i;

            for (int j = 0; j < avgEdgesPerProduct; j++) {
                String source = awsFeatures.get(ThreadLocalRandom.current().nextInt(awsFeatures.size()));
                String target = awsFeatures.get(ThreadLocalRandom.current().nextInt(awsFeatures.size()));

                // Avoid self-dependency and duplicate source-target
                if (!source.equals(target)) {
                    ProductFeatureDependency dep = new ProductFeatureDependency();
                    dep.setId(UUID.randomUUID().toString());
                    dep.setProductId(productId);
                    dep.setSourceFeatureCode(source);
                    dep.setDependentFeatureCode(target);
                    dep.setCreatedAt(LocalDateTime.now());
                    dep.setCreatedBy("DataSeeder");
                    dep.setUpdatedAt(LocalDateTime.now());
                    dep.setUpdatedBy("DataSeeder");

                    allDeps.add(dep);
                }
            }
        }

        repository.saveAll(allDeps);

        return "Inserted " + allDeps.size() + " ProductFeatureDependency records for " + numProducts + " products.";
    }
}
