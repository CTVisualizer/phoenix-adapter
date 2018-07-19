package com.proofpoint.ctvisualizer.executequery.metadata;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MetadataGenerator {

    private List<MetadataGenerationStep> steps = new LinkedList<>();

    public void addMetadataGenerationStep(MetadataGenerationStep step) {
        this.steps.add(step);
    }

    public String generateMetadata(ResultSet resultSet) {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ");
        for(MetadataGenerationStep step: steps) {
            if(step.shouldGenerateMetadata(resultSet)) {
                builder.append(String.format("%s, ",step.generateMetadata(resultSet)));
            }
        }
        builder.delete(builder.length()-2, builder.length()-1);
        builder.append("}");
        return builder.toString();
    }
}
