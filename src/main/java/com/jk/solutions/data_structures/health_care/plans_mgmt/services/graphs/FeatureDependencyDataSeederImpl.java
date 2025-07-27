package com.jk.solutions.data_structures.health_care.plans_mgmt.services.graphs;

import com.jk.solutions.data_structures.health_care.plans_mgmt.entity.ProductFeatureDependency;
import com.jk.solutions.data_structures.health_care.plans_mgmt.repository.ProductFeatureDependencyRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Component
public class FeatureDependencyDataSeederImpl implements FeatureDependencyDataSeeder {

    @Autowired
    private ProductFeatureDependencyRepository repository;

    @SuppressWarnings({"CallToPrintStackTrace", "deprecation"})
    @Override
    public String populateFeatureDependencies(int maxRows, int unusedEdgeFactor) {
        try {
            Reader reader = new InputStreamReader(new ClassPathResource(
                    "Extended_AWS_Service_Feature_Matrix__500_Rows_.csv").getInputStream());
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreEmptyLines()
                    .withTrim());

            Map<String, List<String>> productToFeatures = new LinkedHashMap<>();

            for (CSVRecord record : csvParser) {
                String productId = record.get("product_id");
                String feature = record.get("feature");

                if (productId == null || feature == null) continue;
                productToFeatures
                        .computeIfAbsent(productId.trim(), k -> new ArrayList<>())
                        .add(feature.trim());

                if (productToFeatures.size() > maxRows) break;
            }

            List<ProductFeatureDependency> dependencies = new ArrayList<>();

            for (Map.Entry<String, List<String>> entry : productToFeatures.entrySet()) {
                String productId = entry.getKey();
                List<String> features = entry.getValue();

                // Link each feature to the next feature sequentially
                for (int i = 0; i < features.size() - 1; i++) {
                    dependencies.add(new ProductFeatureDependency(
                            productId,
                            features.get(i),
                            features.get(i + 1)
                    ));
                }
            }

            repository.saveAll(dependencies);
            return "Saved " + dependencies.size() + " product-feature dependency rows.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while populating dependencies: " + e.getMessage();
        }
    }
}
