package com.faltenreich.diaguard.feature.export;

import android.widget.DatePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.hamcrest.Matchers;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExportDatePickerTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(ExportFragment.class);

    @Test
    @Ignore("DatePicker has changed")
    public void onClickingDateButton_shouldOpenDatePicker() {
        Espresso.onView(ViewMatchers.withId(R.id.date_range_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(MaterialDatePicker.class.getName())))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    @Ignore("DatePicker has changed")
    public void pickingDate_shouldApplyToForm() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        Espresso.onView(ViewMatchers.withId(R.id.date_range_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .perform(PickerActions.setDate(start.year().get(), start.getMonthOfYear(), start.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withId(android.R.id.button1))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.date_range_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(start))));
    }
}
