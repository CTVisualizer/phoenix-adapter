package com.proofpoint.ctvisualizer.cliparser;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;
import java.util.Map;

public class CLIParserModule extends AbstractModule {

    private String[] args;

    public CLIParserModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() { }

    @Provides @Named("params")
    Map<String, String> createParams(OptionsGenerator generator) {
        return generator.generateOptions(args);
    }

}
