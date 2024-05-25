package com.faltenreich.diaguard.feature.timeline;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.test.espresso.matcher.ViewMatcher;
import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

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
}
