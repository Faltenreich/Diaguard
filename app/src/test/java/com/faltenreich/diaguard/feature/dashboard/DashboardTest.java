package com.faltenreich.diaguard.feature.dashboard;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.test.espresso.viewaction.NestedScroll;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
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
public class DashboardTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    private FragmentScenario<DashboardFragment> scenario;

    @Before
    public void setup() {
        scenario = FragmentScenario.launchInContainer(DashboardFragment.class);
    }

    @Test
    public void launchingFragment_shouldSucceed() {
        scenario.onFragment(fragment -> Assert.assertTrue(fragment.isAdded()));
    }

    @Test
    public void clickingHbA1c_shouldShowSnackbar() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.hba1c))
            .perform(NestedScroll.nestedScrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
