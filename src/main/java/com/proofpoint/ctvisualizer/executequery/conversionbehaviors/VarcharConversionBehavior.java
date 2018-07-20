package com.proofpoint.ctvisualizer.executequery.conversionbehaviors;

import com.proofpoint.ctvisualizer.executequery.ConversionBehavior;

import java.sql.ResultSet;
import java.sql.SQLException;
import static com.proofpoint.ctvisualizer.Utils.escapeStringValue;

public class VarcharConversionBehavior implements ConversionBehavior {

    @Override
    public String getType() {
        return "VARCHAR";
    }

    @Override
    public String convert(ResultSet resultSet, int columnIndex) {
        try {
            return String.format("\"%s\"", escapeStringValue(resultSet.getString(columnIndex)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
