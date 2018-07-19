package com.proofpoint.ctvisualizer.executequery.conversionbehaviors;

import com.proofpoint.ctvisualizer.executequery.ConversionBehavior;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BinaryConversionBehavior implements ConversionBehavior {
    @Override
    public String getType() {
        return "BINARY";
    }

    @Override
    public String convert(ResultSet resultSet, int columnIndex) {
        try {
            return String.format("\"%s\"",resultSet.getString(columnIndex));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
