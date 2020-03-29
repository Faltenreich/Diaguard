package com.faltenreich.diaguard.feature.timeline;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.ViewPagerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.test.espresso.matcher.ViewMatcher;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.hamcrest.core.AllOf;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TimelineTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final CleanUpData cleanUpData = new CleanUpData();

    @Before
    public void setup() {
        FragmentScenario.launchInContainer(TimelineFragment.class);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
    }

    @Test
    public void holdsThreeDaysAtOnce() {
        Espresso.onView(ViewMatchers.isRoot())
            .check(ViewAssertions.matches(ViewMatcher.withViewCount(ViewMatchers.withId(R.id.day_chart), 3)));
    }

    @Test
    public void onStart_showsToday() {
        String today = DateTimeUtils.toDateString(DateTime.now());
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.day_chart), ViewMatchers.withTagValue(Is.is(today))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void onSwipeLeft_showsYesterday() {
        String yesterday = DateTimeUtils.toDateString(DateTime.now().minusDays(1));

        Espresso.onView(ViewMatchers.withId(R.id.viewpager))
            .perform(ViewPagerActions.scrollLeft());

        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.day_chart), ViewMatchers.withTagValue(Is.is(yesterday))))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
    }

    @Test
    public void onSwipeRight_showsTomorrow() {
        String tomorrow = DateTimeUtils.toDateString(DateTime.now().plusDays(1));

        Espresso.onView(ViewMatchers.withId(R.id.viewpager))
            .perform(ViewPagerActions.scrollRight());

        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.day_chart), ViewMatchers.withTagValue(Is.is(tomorrow))))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
    }
}
