package com.faltenreich.diaguard.test.junit.rule;

import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

public abstract class TestRule implements org.junit.rules.TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                applyBeforeTest();
                List<Throwable> errors = new ArrayList<>();
                try {
                    base.evaluate();
                } catch (Throwable error) {
                    errors.add(error);
                } finally {
                    applyAfterTest();
                }
                MultipleFailureException.assertEmpty(errors);
            }
        };
    }

    public abstract void applyBeforeTest();

    public abstract void applyAfterTest();
}
