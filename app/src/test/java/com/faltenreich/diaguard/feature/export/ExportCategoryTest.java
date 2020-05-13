package com.faltenreich.diaguard.feature.export;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.test.espresso.viewaction.NestedScroll;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class ExportCategoryTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        FragmentScenario.launchInContainer(ExportFragment.class);
    }

    // FIXME
    @Test
    @Ignore("View is not fully visible")
    public void unselectingBloodSugar_shouldDisableCheckboxForHighlightLimits() {
        Espresso.onView(ViewMatchers.withText("Blood Sugar"))
            .perform(NestedScroll.nestedScrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Highlight limits"))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isEnabled())));
    }
}
