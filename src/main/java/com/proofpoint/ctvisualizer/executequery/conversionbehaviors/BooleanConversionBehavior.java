package com.proofpoint.ctvisualizer.executequery.conversionbehaviors;

import com.proofpoint.ctvisualizer.executequery.ConversionBehavior;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanConversionBehavior implements ConversionBehavior {

    @Override
    public String getType() {
        return "BOOLEAN";
    }

    @Override
    public String convert(ResultSet resultSet, int columnIndex) {
        try {
            return String.valueOf(resultSet.getBoolean(columnIndex));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
