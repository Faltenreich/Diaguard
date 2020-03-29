package com.faltenreich.diaguard.feature.dashboard;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.faltenreich.diaguard.test.espresso.viewaction.NestedScroll;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class DashboardTest {

    private ActivityScenario<MainActivity> scenario;

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void clickingLatest_shouldOpenEntryEdit() {
        scenario.onActivity(activity -> {
            Espresso.onView(ViewMatchers.withContentDescription(R.string.measurement_latest))
                .perform(NestedScroll.nestedScrollTo(), ViewActions.click());
            Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
            Assert.assertEquals(EntryEditActivity.class, Shadows.shadowOf(intent).getIntentClass());
        });
    }

    @Test
    public void clickingToday_shouldOpenStatistic() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.today))
            .perform(NestedScroll.nestedScrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.statistics_categories))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void clickingAverage_shouldOpenStatistic() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.average))
            .perform(NestedScroll.nestedScrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.statistics_categories))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void clickingHbA1c_shouldShowSnackbar() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.hba1c))
            .perform(NestedScroll.nestedScrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void clickingTrend_shouldOpenStatistic() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.trend))
            .perform(NestedScroll.nestedScrollTo(), ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.statistics_categories))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
