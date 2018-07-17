package com.proofpoint.ctvisualizer.executequery;

import java.sql.ResultSet;

public interface ConversionBehavior {
    String getType();
    String convert(ResultSet resultSet, int columnIndex);
}
