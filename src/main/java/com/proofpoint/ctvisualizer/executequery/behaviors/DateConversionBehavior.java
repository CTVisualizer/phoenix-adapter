package com.proofpoint.ctvisualizer.executequery.behaviors;

import com.proofpoint.ctvisualizer.executequery.ConversionBehavior;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DateConversionBehavior implements ConversionBehavior {
    @Override
    public String getType() {
        return "DATE";
    }

    @Override
    public String convert(ResultSet resultSet, int columnIndex) {
        try {
            return String.format("\"%s\"", resultSet.getTimestamp(columnIndex));
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
