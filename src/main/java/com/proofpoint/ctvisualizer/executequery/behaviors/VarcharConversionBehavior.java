package com.proofpoint.ctvisualizer.executequery.behaviors;

import com.proofpoint.ctvisualizer.executequery.ConversionBehavior;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VarcharConversionBehavior implements ConversionBehavior {

    @Override
    public String getType() {
        return "VARCHAR";
    }

    @Override
    public String convert(ResultSet resultSet, int columnIndex) {
        try {
            return String.format("\"%s\"",escape(resultSet.getString(columnIndex)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String escape(String input) {
        return input.replaceAll("\\\"", "\\\\\\\"");
    }
}
