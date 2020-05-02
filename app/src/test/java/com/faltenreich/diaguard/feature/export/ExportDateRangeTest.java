package com.faltenreich.diaguard.feature.export;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class ExportDateRangeTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        FragmentScenario.launchInContainer(ExportFragment.class);
        Espresso.onView(ViewMatchers.withId(R.id.date_more_button))
            .perform(ViewActions.click());
    }

    @Test
    public void pickingDateRangeForToday_shouldApplyToForm() {
        DateTime expectedStart = DateTime.now();
        DateTime expectedEnd = DateTime.now();

        Espresso.onView(ViewMatchers.withText(R.string.today))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedStart))));
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedEnd))));
    }

    @Test
    public void pickingDateRangeForCurrentWeek_shouldApplyToForm() {
        DateTime expectedStart = DateTime.now().withDayOfWeek(1);
        DateTime expectedEnd = DateTime.now();

        Espresso.onView(ViewMatchers.withText(R.string.week_current))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedStart))));
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedEnd))));
    }

    @Test
    public void pickingDateRangeForLastTwoWeeks_shouldApplyToForm() {
        DateTime expectedStart = DateTime.now().withDayOfWeek(1).minusWeeks(1);
        DateTime expectedEnd = DateTime.now();

        Espresso.onView(ViewMatchers.withText(R.string.weeks_two))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedStart))));
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedEnd))));
    }

    @Test
    public void pickingDateRangeForLastFourWeeks_shouldApplyToForm() {
        DateTime expectedStart = DateTime.now().withDayOfWeek(1).minusWeeks(3);
        DateTime expectedEnd = DateTime.now();

        Espresso.onView(ViewMatchers.withText(R.string.weeks_four))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedStart))));
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedEnd))));
    }

    @Test
    public void pickingDateRangeForCurrentMonth_shouldApplyToForm() {
        DateTime expectedStart = DateTime.now().withDayOfMonth(1);
        DateTime expectedEnd = DateTime.now();

        Espresso.onView(ViewMatchers.withText(R.string.month_current))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedStart))));
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedEnd))));
    }

    @Test
    public void pickingDateRangeForCurrentQuarter_shouldApplyToForm() {
        DateTime expectedStart = DateTimeUtils.withStartOfQuarter(DateTime.now());
        DateTime expectedEnd = DateTime.now();

        Espresso.onView(ViewMatchers.withText(R.string.quarter_current))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedStart))));
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(expectedEnd))));
    }
}
