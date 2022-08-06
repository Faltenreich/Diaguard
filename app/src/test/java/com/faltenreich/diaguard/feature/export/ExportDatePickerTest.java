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

import org.hamcrest.Matchers;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class ExportDatePickerTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(ExportFragment.class);

    @Test
    public void onClickingDateStartButton_shouldOpenDatePicker() {
        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void pickingStartDate_shouldApplyToForm() {
        LocalDate date = LocalDate.now().minusDays(1);
        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .perform(PickerActions.setDate(date.year().get(), date.getMonthOfYear(), date.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withId(android.R.id.button1))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(date))));
    }

    @Test
    public void onClickingDateEndButton_shouldOpenDatePicker() {
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void pickingEndDate_shouldApplyToForm() {
        LocalDate date = LocalDate.now().plusDays(1);
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .perform(PickerActions.setDate(date.year().get(), date.getMonthOfYear(), date.getDayOfMonth()));
        Espresso.onView(ViewMatchers.withId(android.R.id.button1))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(date))));
    }
}
