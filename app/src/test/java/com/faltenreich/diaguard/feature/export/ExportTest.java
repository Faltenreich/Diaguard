package com.faltenreich.diaguard.feature.export;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.test.junit.rule.ApplyAppTheme;
import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class ExportTest {

    @Rule public final ApplyAppTheme applyAppTheme = new ApplyAppTheme();
    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        FragmentScenario.launchInContainer(ExportFragment.class);
    }

    @Test
    public void onStart_shouldStartAtStartOfWeekAndEndAtToday() {
        String expectedStart = Helper.getDateFormat().print(DateTime.now().withDayOfWeek(1));
        String expectedEnd = Helper.getDateFormat().print(DateTime.now());
        String expectedLabel = String.format("%s - %s", expectedStart, expectedEnd);
        Espresso.onView(ViewMatchers.withId(R.id.date_range_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(expectedLabel)));
    }

    @Test
    public void onStart_shouldSelectPdf() {
        Espresso.onView(ViewMatchers.withId(R.id.format_spinner))
            .check(ViewAssertions.matches(ViewMatchers.withSpinnerText("PDF")));
    }
}
