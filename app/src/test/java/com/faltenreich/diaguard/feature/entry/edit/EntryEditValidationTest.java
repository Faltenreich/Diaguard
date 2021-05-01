package com.faltenreich.diaguard.feature.entry.edit;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditValidationTest {

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    private ActivityScenario<EntryEditActivity> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(EntryEditActivity.class);
    }

    @Test
    public void confirmingEmptyEntry_shouldKeepSceneOpen() {
        scenario.onActivity(activity -> {
            Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());
            Assert.assertNull(Shadows.shadowOf(activity).getNextStartedActivity());
        });
    }

    @Test
    public void confirmingEmptyEntry_shouldShowWarning() {
        Espresso.onView(ViewMatchers.withId(R.id.fab))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.validator_value_none)));
    }

    @Test
    public void confirmingEntryWithNote_shouldFinishActivity() {
        scenario.onActivity(activity -> {
            Espresso.onView(ViewMatchers.withId(R.id.note_input))
                .perform(ViewActions.replaceText("Test"));
            Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());
            Assert.assertTrue(activity.isFinishing());
        });
    }

    @Test
    public void confirmingEntryWithTag_shouldFinishActivity() {
        scenario.onActivity(activity -> {
            String tag = "new tag";
            Espresso.onView(ViewMatchers.withId(R.id.tag_input))
                .perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.tag_input)).perform(
                ViewActions.replaceText(tag),
                ViewActions.pressImeActionButton()
            );
            Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());
            Assert.assertTrue(activity.isFinishing());
        });
    }
}
