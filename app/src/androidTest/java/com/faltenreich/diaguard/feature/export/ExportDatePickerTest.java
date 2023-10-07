package com.faltenreich.diaguard.feature.export;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExportDatePickerTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(ExportFragment.class);

    @Test
    public void onClickingDateButton_shouldOpenDateRangePicker() {
        Espresso.onView(ViewMatchers.withId(R.id.date_range_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.mtrl_picker_title_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void pickingDate_shouldApplyToForm() {
        Espresso.onView(ViewMatchers.withId(R.id.date_range_button))
            .perform(ViewActions.click());

        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        DateTimeUtils.setDateRange(start, end);

        String expected = String.format("%s  -  %s",
            Helper.getDateFormat().print(start),
            Helper.getDateFormat().print(end));
        Espresso.onView(ViewMatchers.withId(R.id.date_range_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(expected)));
    }
}
