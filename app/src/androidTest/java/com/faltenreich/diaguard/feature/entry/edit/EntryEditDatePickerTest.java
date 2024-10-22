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

import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditDatePickerTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(EntryEditFragment.class);

    @Test
    @Ignore("Fails on Android 29 but succeeds on Android 21")
    public void pickingDate_shouldApplyToForm() {
        Espresso.onView(ViewMatchers.withId(R.id.date_button))
            .perform(ViewActions.click());

        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeUtils.setDate(date);

        String expected = Helper.getDateFormat().print(date);
        Espresso.onView(ViewMatchers.withId(R.id.date_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(expected)));
    }
}
