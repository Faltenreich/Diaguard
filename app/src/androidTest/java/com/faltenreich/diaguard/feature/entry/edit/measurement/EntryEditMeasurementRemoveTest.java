package com.faltenreich.diaguard.feature.entry.edit.measurement;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditMeasurementRemoveTest {

    private static final String CATEGORY = "Blood Sugar";

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
        EntryEditMeasurementTestUtils.openFloatingMenuForCategories();
        Espresso.onView(ViewMatchers.withText(CATEGORY))
            .perform(ViewActions.click());
    }

    @Test
    public void clickingRemoveButton_shouldRemoveMeasurement() {
        Espresso.onView(ViewMatchers.withContentDescription("Remove " + CATEGORY))
            .perform(ViewActions.click());
        ensureMeasurementCount(0);
    }

    @Test
    public void unselectingViaPicker_shouldRemoveMeasurement() {
        EntryEditMeasurementTestUtils.openFloatingMenuForCategories();
        EntryEditMeasurementTestUtils.openPickerForCategories();

        Espresso.onView(ViewMatchers.withText(CATEGORY))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("OK"))
            .perform(ViewActions.click());

        ensureMeasurementCount(0);
    }

    private void ensureMeasurementCount(int childCount) {
        Espresso.onView(ViewMatchers.withId(R.id.measurement_container))
            .check(ViewAssertions.matches(ViewMatchers.hasChildCount(childCount)));
    }
}
