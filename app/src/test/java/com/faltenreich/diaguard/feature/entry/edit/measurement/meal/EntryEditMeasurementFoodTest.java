package com.faltenreich.diaguard.feature.entry.edit.measurement.meal;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditMeasurementFoodTest {

    private ActivityScenario<EntryEditActivity> scenario;

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), EntryEditActivity.class);
        intent.putExtra(EntryEditActivity.EXTRA_CATEGORY, Category.MEAL);
        scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void clickingFoodButton_shouldOpenFoodActivity() {
        scenario.onActivity(activity -> {
            Espresso.onView(ViewMatchers.withText("Add food"))
                .perform(ViewActions.click());
            Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
            // FIXME: Assert.assertEquals(FoodActivity.class, Shadows.shadowOf(intent).getIntentClass());
        });
    }
}
