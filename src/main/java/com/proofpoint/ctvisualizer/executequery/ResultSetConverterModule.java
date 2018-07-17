package com.proofpoint.ctvisualizer.executequery;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.proofpoint.ctvisualizer.executequery.behaviors.*;

public class ResultSetConverterModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    private ResultSetConverter provideResultSetConverter() {
        ResultSetConverter resultSetConverter = new ResultSetConverter();
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new VarcharConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new BooleanConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new VarcharArrayConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new DateConversionBehavior()));
        return resultSetConverter;
    }
}
