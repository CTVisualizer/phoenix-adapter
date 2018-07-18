package com.proofpoint.ctvisualizer.cliparser;

import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OptionsGenerator {


    public Map<String, String> generateOptions(String[] args) {
        Map<String, String> params = new HashMap<>();
        Options options = new Options();
        options.addRequiredOption("quorum", "quorum", true, "zookeeper quorum");
        options.addRequiredOption("port", "port", true, "zookeeper quorum port");
        options.addRequiredOption("hbaseNode", "hbaseNode", true, "hbase node");
        options.addRequiredOption("principal", "principal", true, "kerberos principal");
        options.addRequiredOption("keytab", "keytab", true, "absolute path to keytab");
        options.addRequiredOption("phoenixClient", "phoenixClient", true, "Phoenix Client jar");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            Iterator<Option> iter = commandLine.iterator();
            while(iter.hasNext()) {
                Option currentOption = iter.next();
                params.put(currentOption.getOpt(), currentOption.getValue());
            }
            return params;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
