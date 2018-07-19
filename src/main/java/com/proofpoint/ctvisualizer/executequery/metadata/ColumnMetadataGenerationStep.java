package com.proofpoint.ctvisualizer.executequery.metadata;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ColumnMetadataGenerationStep implements MetadataGenerationStep {

    @Override
    public String generateMetadata(ResultSet resultSet) {
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            StringBuilder builder = new StringBuilder();
            builder.append("\"columns\": ");
            builder.append("[ ");
            for(int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                builder.append("{ ");
                builder.append(String.format("\"name\": \"%s\", ", resultSetMetaData.getColumnName(i)));
                builder.append(String.format("\"type\": \"%s\"", resultSetMetaData.getColumnTypeName(i)));
                builder.append(" }, ");
            }
            builder.delete(builder.length()-2, builder.length()-1);
            builder.append("]");
            return builder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shouldGenerateMetadata(ResultSet resultSet) {
        return true;
    }
}
