package com.proofpoint.ctvisualizer.executequery.metadata;

import java.sql.ResultSet;

public interface MetadataGenerationStep {
    String generateMetadata(ResultSet resultSet);
    boolean shouldGenerateMetadata(ResultSet resultSet);
}
