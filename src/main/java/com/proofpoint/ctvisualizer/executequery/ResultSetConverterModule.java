package com.proofpoint.ctvisualizer.executequery;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.proofpoint.ctvisualizer.executequery.behaviors.*;

import javax.inject.Named;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResultSetConverterModule extends AbstractModule {

    @Override
    protected void configure() { }

    @Provides @Singleton @Named("should-stop-flag")
    AtomicBoolean provideShouldStopFlag() {
        return new AtomicBoolean(true);
    }

    @Provides
    private ResultSetConverter provideResultSetConverter(@Named("should-stop-flag") AtomicBoolean shouldStop) {
        ResultSetConverter resultSetConverter = new ResultSetConverter(shouldStop);
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new VarcharConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new BooleanConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new VarcharArrayConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new DateConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new BinaryConversionBehavior()));
        return resultSetConverter;
    }
}
