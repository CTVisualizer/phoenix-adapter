package com.proofpoint.ctvisualizer;

public class Utils {

    public static String escapeQuotes(String input) {
        return input.replaceAll("\\\"", "\\\\\\\"");
    }
}
