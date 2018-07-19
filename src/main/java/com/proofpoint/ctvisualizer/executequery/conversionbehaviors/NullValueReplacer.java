package com.proofpoint.ctvisualizer.executequery.conversionbehaviors;

import com.proofpoint.ctvisualizer.executequery.ConversionBehavior;

import java.sql.ResultSet;

public class NullValueReplacer extends AbstractConversionBehaviorDecorator{


    public NullValueReplacer(ConversionBehavior underlyingConversionBehavior) {
        super(underlyingConversionBehavior);
    }

    @Override
    public String convert(ResultSet resultSet, int columnIndex) {
        try {
            return super.convert(resultSet, columnIndex);
        } catch (NullPointerException e) {
            return "null";
        }

    }
}
