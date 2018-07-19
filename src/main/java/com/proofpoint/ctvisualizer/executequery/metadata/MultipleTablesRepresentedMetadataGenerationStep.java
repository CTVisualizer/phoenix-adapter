package com.proofpoint.ctvisualizer.executequery.metadata;

import java.sql.ResultSet;
import static com.proofpoint.ctvisualizer.Utils.tablesRepresentedBy;

public class MultipleTablesRepresentedMetadataGenerationStep implements MetadataGenerationStep {
    @Override
    public String generateMetadata(ResultSet resultSet) {
        return String.format("\"multipleTablesRepresented\": %s",
                String.valueOf(tablesRepresentedBy(resultSet).size() != 1));
    }

    @Override
    public boolean shouldGenerateMetadata(ResultSet resultSet) {
        return true;
    }
}
