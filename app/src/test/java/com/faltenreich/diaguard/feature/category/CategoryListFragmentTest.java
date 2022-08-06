package com.faltenreich.diaguard.feature.category;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class CategoryListFragmentTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(CategoryListFragment.class);

    @Test
    public void launchingFragment_shouldSucceed() {

    }
}
