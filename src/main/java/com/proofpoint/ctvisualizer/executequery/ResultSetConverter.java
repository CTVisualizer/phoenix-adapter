package com.proofpoint.ctvisualizer.executequery;

import com.google.inject.name.Named;
import com.proofpoint.ctvisualizer.executequery.metadata.MetadataGenerator;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResultSetConverter {

    private AtomicBoolean shouldStop;
    private ConversionBehavior defaultConversionBehavior;
    private MetadataGenerator metadataGenerator;

    @Inject
    public ResultSetConverter(@Named("should-stop-flag") AtomicBoolean shouldStop,
                              @Named("default-conversion-behavior") ConversionBehavior defaultConversionBehavior,
                              MetadataGenerator metadataGenerator) {
        this.shouldStop = shouldStop;
        this.defaultConversionBehavior = defaultConversionBehavior;
        this.metadataGenerator = metadataGenerator;

    }

    Map<String, ConversionBehavior> conversionBehaviors = new HashMap<>();

    public void addConversionBehavior(ConversionBehavior behavior) {
        conversionBehaviors.put(behavior.getType(), behavior);
    }

    public String convert(ResultSet resultSet) {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ");
        builder.append(String.format("\"metadata\": %s, ", metadataGenerator.generateMetadata(resultSet)));
        builder.append(String.format("\"data\": %s ", convertQueryData(resultSet)));
        builder.append("}");
        return builder.toString();
    }

    private String convertQueryData(ResultSet resultSet) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("[ ");
            boolean empty = true;
            while(resultSet.next()) {
                empty = false;
                builder.append(String.format("%s, ", convertRow(resultSet)));
            }
            if (!empty) {
                builder.delete(builder.length()-2, builder.length()-1);
            }
            builder.append("]");
            return builder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertRow(ResultSet resultSet) {
        try {
            if (shouldStop.get()) {
                throw new RuntimeException("Stopped Execution");
            }
            StringBuilder builder = new StringBuilder();
            builder.append("{ ");
            int numColumns = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                builder.append(String.format("%s, ", convertColumn(resultSet, i)));
            }
            builder.delete(builder.length()-2, builder.length()-1);
            builder.append("}");
            return builder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    String convertColumn(ResultSet resultSet, int columnIndex) {
        try {
            String columnType = resultSet.getMetaData().getColumnTypeName(columnIndex);
            String key = resultSet.getMetaData().getColumnLabel(columnIndex);
            String value;
            if (conversionBehaviors.containsKey(columnType)) {
                ConversionBehavior conversionBehavior = conversionBehaviors.get(columnType);
                value = conversionBehavior.convert(resultSet, columnIndex);
            } else {
                value = defaultConversionBehavior.convert(resultSet, columnIndex);
            }

            return String.format("\"%s\": %s", key, value);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
