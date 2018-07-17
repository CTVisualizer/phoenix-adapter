package com.proofpoint.ctvisualizer.executequery.behaviors;

import com.proofpoint.ctvisualizer.executequery.ConversionBehavior;

import java.sql.ResultSet;

public abstract class AbstractConversionBehaviorDecorator implements ConversionBehavior {

    private ConversionBehavior underlyingConversionBehavior;

    public AbstractConversionBehaviorDecorator(ConversionBehavior underlyingConversionBehavior) {
        this.underlyingConversionBehavior = underlyingConversionBehavior;
    }

    @Override
    public String getType() {
        return underlyingConversionBehavior.getType();
    }

    @Override
    public String convert(ResultSet resultSet, int columnIndex) {
        return underlyingConversionBehavior.convert(resultSet, columnIndex);
    }
}
