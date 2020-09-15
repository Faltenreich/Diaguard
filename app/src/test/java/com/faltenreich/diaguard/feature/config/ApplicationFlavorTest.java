package com.faltenreich.diaguard.feature.config;

import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationFlavorTest {

    @Test
    public void identifierMatchesDemo() {
        assertEquals(ApplicationFlavor.fromIdentifier("demo"), ApplicationFlavor.DEMO);
    }

    @Test
    public void identifierMatchesBeta() {
        assertEquals(ApplicationFlavor.fromIdentifier("beta"), ApplicationFlavor.BETA);
    }

    @Test
    public void identifierMatchesProduction() {
        assertEquals(ApplicationFlavor.fromIdentifier("production"), ApplicationFlavor.PRODUCTION);
    }

    @Test
    public void calculatorIsDisabledForDemo() {
        assertFalse(ApplicationFlavor.DEMO.isCalculatorEnabled());
    }

    @Test
    public void calculatorIsEnabledForBeta() {
        assertTrue(ApplicationFlavor.BETA.isCalculatorEnabled());
    }

    @Test
    public void calculatorIsDisabledForProduction() {
        assertFalse(ApplicationFlavor.PRODUCTION.isCalculatorEnabled());
    }

    @Test
    public void doImportDemoDataForDemo() {
        assertTrue(ApplicationFlavor.DEMO.importDemoData());
    }

    @Test
    public void doNotImportDemoDataForBeta() {
        assertFalse(ApplicationFlavor.BETA.importDemoData());
    }

    @Test
    public void doNotImportDemoDataForProduction() {
        assertFalse(ApplicationFlavor.PRODUCTION.importDemoData());
    }
}