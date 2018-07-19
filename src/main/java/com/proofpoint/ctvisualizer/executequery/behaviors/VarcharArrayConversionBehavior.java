package com.proofpoint.ctvisualizer.executequery.behaviors;

import com.proofpoint.ctvisualizer.executequery.ConversionBehavior;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.proofpoint.ctvisualizer.Utils.escapeQuotes;

public class VarcharArrayConversionBehavior implements ConversionBehavior {


    @Override
    public String getType() {
        return "VARCHAR ARRAY";
    }

    @Override
    public String convert(ResultSet resultSet, int columnIndex) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("[ ");
            String[] values = (String[])resultSet.getArray(columnIndex).getArray();
            for(String value: values) {
                builder.append(String.format("\"%s\", ", escapeQuotes(value)));
            }
            builder.delete(builder.length()-2, builder.length()-1);
            builder.append("]");
            return builder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
