package com.proofpoint.ctvisualizer.executequery.behaviors;

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
            byte[] binaryBytes = resultSet.getBytes(columnIndex);
            return "";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
