package com.faltenreich.diaguard.feature.entry.edit;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.codetroopers.betterpickers.numberpicker.NumberPicker;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;
import com.faltenreich.diaguard.test.junit.rule.GrantRuntimePermissions;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditAlarmTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();
    @Rule public final TestRule grantRuntimePermissions = new GrantRuntimePermissions();

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
    }

    @Test
    public void clickingAlarmButton_shouldOpenNumberPicker() {
        Espresso.onView(ViewMatchers.withId(R.id.alarm_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(NumberPicker.class.getName())))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void pickingNumber_shouldUpdateEntryForm() {
        Espresso.onView(ViewMatchers.withId(R.id.alarm_button))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("1"))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("2"))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.ok))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.alarm_button))
            .check(ViewAssertions.matches(ViewMatchers.withText("Reminder in 12 Minutes")));
    }
}
