package com.proofpoint.ctvisualizer.external;

import org.apache.commons.cli.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CommonsCLITest {

    @Test
    public void testCommonsCLI() throws ParseException {
        String version = "apache-phoenix-4.13.2-cdh5.11.2";
        String[] args = {String.format("-p=%s", version)};

        Options options = new Options();
        options.addRequiredOption("p", "phoenixDriverVersion", true, "version of phoenix driver to use");

        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, args);
        String result = cl.getOptionValue("p");
        assertEquals(version, result);
    }


}
