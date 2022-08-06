package com.faltenreich.diaguard.feature.log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class LogTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(LogFragment.class);

    @Before
    public void setup() {
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
    }

    @Test
    public void showsCurrentMonthOnStart() {
        DateTime today = DateTime.now();
        String month = today.toString("MMM YYYY");
        Espresso.onView(ViewMatchers.withText(month))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    @Ignore("RecyclerView items are not visible")
    public void showsTodayOnStart() {
        DateTime today = DateTime.now();

        String day = today.toString("dd");
        Espresso.onView(ViewMatchers.withText(day))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        String weekDay = today.dayOfWeek().getAsShortText();
        Espresso.onView(ViewMatchers.withText(weekDay))
            .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.isDisplayed())));
    }
}
