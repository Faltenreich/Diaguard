package com.faltenreich.diaguard.feature.timeline.day;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.timeline.TimelineDayFragment;
import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TimelineDayTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(TimelineDayFragment.class);

    @Test
    public void showsChart() {
        Espresso.onView(ViewMatchers.withId(R.id.chart_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void showsTable() {
        Espresso.onView(ViewMatchers.withId(R.id.image_list_view))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.onView(ViewMatchers.withId(R.id.value_list_view))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
