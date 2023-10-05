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
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditMeasurementCategoryTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
    }

    @Test
    public void selectingMultipleCategoriesViaPicker_shouldAppendToMeasurementList() {
        EntryEditMeasurementTestUtils.openPickerForCategories();

        Espresso.onView(ViewMatchers.withText("Blood Sugar"))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Insulin"))
            .inRoot(RootMatchers.isDialog())
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("OK"))
            .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("Blood Sugar"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("Insulin"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
