package com.faltenreich.diaguard.feature.entry.edit;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EntryEditValidationTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    private FragmentScenario<EntryEditFragment> scenario;

    @Before
    public void setup() {
        scenario = FragmentScenario.launchInContainer(EntryEditFragment.class);
    }

    @Test
    public void launchingFragment_shouldSucceed() {
        scenario.onFragment(fragment -> Assert.assertTrue(fragment.isAdded()));
    }

    @Ignore("Fails if executed with other tests")
    @Test
    public void confirmingEmptyEntry_shouldShowWarning() {
        Espresso.onView(ViewMatchers.withId(R.id.fab))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.validator_value_none)));
    }
}
