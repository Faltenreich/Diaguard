package com.faltenreich.diaguard.test.junit.rule;

import androidx.fragment.app.Fragment;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public class TestRuleFactory {

    public static <T extends Fragment> TestRule forFragment(Class<T> clazz) {
        return RuleChain
            .outerRule(new ApplyAppTheme())
            .around(new CleanUpData())
            .around(new FragmentRule<>(clazz));
    }
}
