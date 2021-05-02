package com.faltenreich.diaguard.feature.category;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;
import com.faltenreich.diaguard.test.junit.rule.TestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class CategoryListFragmentTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    private FragmentScenario<CategoryListFragment> scenario;

    @Before
    public void setup() {
        scenario = FragmentScenario.launchInContainer(CategoryListFragment.class);
    }

    @Test
    public void launchingFragment_shouldSucceed() {
        scenario.onFragment(fragment -> Assert.assertTrue(fragment.isAdded()));
    }
}
