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

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditTimePickerTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
    }

    @Test
    public void clickingTimeButton_shouldOpenTimePicker() {
        Espresso.onView(ViewMatchers.withId(R.id.time_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.header_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

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
