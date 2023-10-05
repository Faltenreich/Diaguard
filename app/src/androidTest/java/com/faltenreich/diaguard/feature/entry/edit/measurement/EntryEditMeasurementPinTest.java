package com.faltenreich.diaguard.feature.entry.edit.measurement;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.faltenreich.diaguard.R;
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
public class EntryEditMeasurementPinTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.fab_primary))
            .perform(ViewActions.click());
        EntryEditMeasurementTestUtils.addCategory(Category.BLOODSUGAR);
    }

    @Test
    public void clickingCheckBoxOnce_shouldShowSnackbarWithConfirmedPin() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.category_pin))
            .perform(ViewActions.click());
        SnackbarUtils.assertDisplayedSnackbar("Blood Sugar has been pinned");
    }

    @Test
    public void clickingCheckBoxTwice_shouldShowSnackbarWithConfirmedUnpin() {
        Espresso.onView(ViewMatchers.withContentDescription(R.string.category_pin))
            .perform(ViewActions.click(), ViewActions.click());
        SnackbarUtils.assertDisplayedSnackbar("Blood Sugar has been unpinned");
    }
}
