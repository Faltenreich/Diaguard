package com.faltenreich.diaguard.feature.entry.edit.measurement.pressure;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.measurement.EntryEditMeasurementTestUtils;
import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.test.espresso.SnackbarUtils;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EntryEditMeasurementPressureTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
        EntryEditMeasurementTestUtils.addCategory(Category.PRESSURE);
    }

    @Test
    public void confirmingEmptyValue_shouldShowWarning() {
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
        SnackbarUtils.assertDisplayedSnackbar(R.string.validator_value_none);
    }

    @Test
    public void confirmingValidValues_shouldSucceed() {
        Espresso.onView(ViewMatchers.withHint(R.string.systolic))
            .perform(ViewActions.replaceText("100"));
        Espresso.onView(ViewMatchers.withHint(R.string.diastolic))
            .perform(ViewActions.replaceText("100"));
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withHint(R.string.systolic))
            .check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withHint(R.string.diastolic))
            .check(ViewAssertions.doesNotExist());
    }
}
