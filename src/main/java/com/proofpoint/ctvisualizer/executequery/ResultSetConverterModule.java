package com.proofpoint.ctvisualizer.executequery;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.proofpoint.ctvisualizer.executequery.conversionbehaviors.*;
import com.proofpoint.ctvisualizer.executequery.metadata.ColumnMetadataGenerationStep;
import com.proofpoint.ctvisualizer.executequery.metadata.MetadataGenerator;
import com.proofpoint.ctvisualizer.executequery.metadata.MultipleTablesRepresentedMetadataGenerationStep;
import com.proofpoint.ctvisualizer.executequery.metadata.PrimaryKeyMetadataGenerationStep;

import javax.inject.Named;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResultSetConverterModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Named("should-stop-flag")
    AtomicBoolean provideShouldStopFlag() {
        return new AtomicBoolean(true);
    }

    @Provides
    @Named("default-conversion-behavior")
    ConversionBehavior provideDefaultConversionBehavior() {
        return new NullValueReplacer(new VarcharConversionBehavior());
    }

    @Provides
    private ResultSetConverter provideResultSetConverter(@Named("should-stop-flag") AtomicBoolean shouldStop,
                                                         @Named("default-conversion-behavior") ConversionBehavior defaultConversionBehavior,
                                                         MetadataGenerator metadataGenerator) {
        ResultSetConverter resultSetConverter = new ResultSetConverter(shouldStop, defaultConversionBehavior, metadataGenerator);
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new VarcharConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new BooleanConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new VarcharArrayConversionBehavior()));
        resultSetConverter.addConversionBehavior(new NullValueReplacer(new DateConversionBehavior()));
        return resultSetConverter;
    }

    @Provides
    private MetadataGenerator provideMetadataGenerator() {
        MetadataGenerator metadataGenerator = new MetadataGenerator();
        metadataGenerator.addMetadataGenerationStep(new ColumnMetadataGenerationStep());
        metadataGenerator.addMetadataGenerationStep(new MultipleTablesRepresentedMetadataGenerationStep());
        metadataGenerator.addMetadataGenerationStep(new PrimaryKeyMetadataGenerationStep());
        return metadataGenerator;
    }
}
