package com.faltenreich.diaguard.feature.export;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class ExportTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(ExportFragment.class);

    @Test
    public void onStart_shouldStartAtStartOfWeek() {
        String date = Helper.getDateFormat().print(DateTime.now().withDayOfWeek(1));
        Espresso.onView(ViewMatchers.withId(R.id.date_start_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(date)));
    }

    @Test
    public void onStart_shouldEndAtToday() {
        String date = Helper.getDateFormat().print(DateTime.now());
        Espresso.onView(ViewMatchers.withId(R.id.date_end_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(date)));
    }

    @Test
    public void onStart_shouldSelectPdf() {
        Espresso.onView(ViewMatchers.withId(R.id.format_spinner))
            .check(ViewAssertions.matches(ViewMatchers.withSpinnerText("PDF")));
    }
}
