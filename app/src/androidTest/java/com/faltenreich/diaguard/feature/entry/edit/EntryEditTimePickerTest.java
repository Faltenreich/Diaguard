package com.faltenreich.diaguard.feature.entry.edit;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.test.espresso.DateTimeUtils;
import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

import org.joda.time.LocalTime;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditTimePickerTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(EntryEditFragment.class);

    @Test
    @Ignore("Ignore until DateTimeUtils.setTime has been fixed")
    public void pickingTime_shouldApplyToForm() {
        LocalTime time = LocalTime.now().minusHours(1);
        Espresso.onView(ViewMatchers.withId(R.id.time_button))
            .perform(ViewActions.click());
        DateTimeUtils.setTime(time);
        Espresso.onView(ViewMatchers.withId(R.id.time_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getTimeFormat().print(time))));
    }
}
