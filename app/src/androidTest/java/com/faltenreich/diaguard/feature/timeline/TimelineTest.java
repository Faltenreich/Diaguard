package com.faltenreich.diaguard.feature.timeline;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.ViewPagerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.test.espresso.matcher.ViewMatcher;
import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

import org.hamcrest.core.AllOf;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TimelineTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(TimelineFragment.class);

    @Test
    public void holdsThreeDaysAtOnce() {
        Espresso.onView(ViewMatchers.isRoot())
            .check(ViewAssertions.matches(ViewMatcher.withViewCount(ViewMatchers.withId(R.id.chart_view), 3)));
    }

    @Ignore("Fails due to label not visible")
    @Test
    public void onStart_showsToday() {
        String today = DateTimeUtils.toDateString(DateTime.now());
        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.chart_view), ViewMatchers.withTagValue(Is.is(today))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Ignore("Fails due to label not visible")
    @Test
    public void onSwipeLeft_showsYesterday() {
        String yesterday = DateTimeUtils.toDateString(DateTime.now().minusDays(1));

        Espresso.onView(ViewMatchers.withId(R.id.view_pager))
            .perform(ViewPagerActions.scrollLeft());

        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.chart_view), ViewMatchers.withTagValue(Is.is(yesterday))))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
    }

    @Ignore("Fails due to label not visible")
    @Test
    public void onSwipeRight_showsTomorrow() {
        String tomorrow = DateTimeUtils.toDateString(DateTime.now().plusDays(1));

        Espresso.onView(ViewMatchers.withId(R.id.view_pager))
            .perform(ViewPagerActions.scrollRight());

        Espresso.onView(AllOf.allOf(ViewMatchers.withId(R.id.chart_view), ViewMatchers.withTagValue(Is.is(tomorrow))))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()));
    }
}
