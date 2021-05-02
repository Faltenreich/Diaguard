package com.faltenreich.diaguard.feature.entry.edit.measurement;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.test.junit.rule.CleanUpData;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditMeasurementRemoveTest {

    private static final String CATEGORY = "Blood Sugar";

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(EntryEditActivity.class);
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
    public void swipingCardLeft_shouldRemoveMeasurement() {
        Espresso.onView(ViewMatchers.withText(CATEGORY))
            .perform(ViewActions.swipeLeft());
        ensureMeasurementCount(0);
    }

    @Test
    public void swipingCardRight_shouldRemoveMeasurement() {
        Espresso.onView(ViewMatchers.withText(CATEGORY))
            .perform(ViewActions.swipeRight());
        ensureMeasurementCount(0);
    }

    @Test
    public void swipingCardUp_shouldDoNothing() {
        Espresso.onView(ViewMatchers.withText(CATEGORY))
            .perform(ViewActions.swipeUp());
        ensureMeasurementCount(1);
    }

    @Test
    public void swipingCardDown_shouldDoNothing() {
        Espresso.onView(ViewMatchers.withText(CATEGORY))
            .perform(ViewActions.swipeDown());
        ensureMeasurementCount(1);
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
