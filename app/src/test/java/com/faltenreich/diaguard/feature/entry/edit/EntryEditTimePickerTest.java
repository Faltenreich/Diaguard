package com.faltenreich.diaguard.feature.entry.edit;

import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.test.junit.rule.CleanUpData;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.Helper;

import org.hamcrest.Matchers;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditTimePickerTest {

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(EntryEditActivity.class);
    }

    @Test
    public void clickingTimeButton_shouldOpenTimePicker() {
        Espresso.onView(ViewMatchers.withId(R.id.time_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void pickingTime_shouldApplyToForm() {
        LocalTime time = LocalTime.now().minusHours(1);
        Espresso.onView(ViewMatchers.withId(R.id.time_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName())))
            .inRoot(RootMatchers.isDialog())
            .perform(PickerActions.setTime(time.hourOfDay().get(), time.minuteOfHour().get()));
        Espresso.onView(ViewMatchers.withId(android.R.id.button1))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.time_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(Helper.getTimeFormat().print(time))));
    }
}
