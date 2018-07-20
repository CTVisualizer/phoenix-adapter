package com.proofpoint.ctvisualizer.external;

import com.proofpoint.ctvisualizer.Utils;
import org.junit.jupiter.api.Test;

public class StringEscapeTest {

    @Test
    public void testStringEscape() {
        System.out.println(Utils.escapeStringValue("\n\n\n"));
    }
}
