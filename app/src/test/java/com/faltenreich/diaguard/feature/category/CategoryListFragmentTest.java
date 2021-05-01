package com.faltenreich.diaguard.feature.category;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class CategoryListFragmentTest {

    private FragmentScenario<CategoryListFragment> scenario;

    @Rule
    public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        scenario = FragmentScenario.launch(CategoryListFragment.class);
    }

    @Test
    public void canBeOpened() {
        scenario.onFragment(fragment -> {
            Assert.assertEquals(true, true);
        });
    }
}
