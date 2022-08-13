package com.faltenreich.diaguard.feature.export;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.faltenreich.diaguard.test.junit.rule.TestRuleFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class ExportPdfTest {

    @Rule
    public final TestRule rule = TestRuleFactory.forFragment(ExportFragment.class);

    @Before
    public void setup() {
        ExportTestUtils.selectFileType(FileType.PDF);
    }

    @Test
    public void selectingPDF_shouldShowStyleSpinner() {
        Espresso.onView(ViewMatchers.withId(R.id.style_spinner))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void selectingPDF_shouldShowHeaderCheckBox() {
        Espresso.onView(ViewMatchers.withId(R.id.header_checkbox))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void selectingPDF_shouldShowFooterCheckBox() {
        Espresso.onView(ViewMatchers.withId(R.id.header_checkbox))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
