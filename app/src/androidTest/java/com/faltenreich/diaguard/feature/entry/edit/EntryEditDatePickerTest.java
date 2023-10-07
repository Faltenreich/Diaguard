package com.faltenreich.diaguard.feature.entry.edit;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.test.espresso.DateTimeUtils;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditDatePickerTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
    }

    @Test
    public void clickingDateButton_shouldOpenDatePicker() {
        Espresso.onView(ViewMatchers.withId(R.id.date_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.mtrl_picker_title_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void pickingDate_shouldApplyToForm() {
        LocalDate date = LocalDate.now().minusDays(1);
        Espresso.onView(ViewMatchers.withId(R.id.date_button))
            .perform(ViewActions.click());
        DateTimeUtils.setDate(date);
        Espresso.onView(ViewMatchers.withId(R.id.date_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getDateFormat().print(date))));
    }
}
