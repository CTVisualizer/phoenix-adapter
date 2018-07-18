package com.proofpoint.ctvisualizer.executequery;

import com.google.inject.name.Named;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class ResultSetConverter {

    private AtomicBoolean shouldStop;

    @Inject
    public ResultSetConverter(@Named("should-stop-flag") AtomicBoolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    Map<String, ConversionBehavior> conversionBehaviors = new HashMap<>();

    public void addConversionBehavior(ConversionBehavior behavior) {
        conversionBehaviors.put(behavior.getType(), behavior);
    }

    public String convert(ResultSet resultSet) {
        try {
//            Logger.getLogger(this.getClass().getName()).info("Should stop: "+ shouldStop.get());
            StringBuilder builder = new StringBuilder();
            builder.append("{ ");
            builder.append(String.format("\"metadata\": %s, ", convertMetaData(resultSet.getMetaData())));
            builder.append(String.format("\"data\": %s ", convertQueryData(resultSet)));
            builder.append("}");
            return builder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private String convertMetaData(ResultSetMetaData resultSetMetaData) {
        return "{ " +
                String.format("\"columns\": %s", convertColumnMetaData(resultSetMetaData)) +
                " }";
    }

    private String convertColumnMetaData(ResultSetMetaData resultSetMetaData) {
        try {
            StringBuilder builder = new StringBuilder();
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

    private String convertQueryData(ResultSet resultSet) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("[ ");
            while(resultSet.next()) {
                builder.append(String.format("%s, ", convertRow(resultSet)));
            }
            builder.delete(builder.length()-2, builder.length()-1);
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
            String key = resultSet.getMetaData().getColumnName(columnIndex);
            String value;
            if (conversionBehaviors.containsKey(columnType)) {
                ConversionBehavior conversionBehavior = conversionBehaviors.get(columnType);
                value = conversionBehavior.convert(resultSet, columnIndex);
            } else {
                throw new RuntimeException(String.format("No conversion behavior for column type: %s",columnType));
            }

            return String.format("\"%s\": %s", key, value);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
