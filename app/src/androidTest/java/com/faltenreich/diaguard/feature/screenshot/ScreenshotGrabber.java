package com.faltenreich.diaguard.feature.screenshot;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.locale.LocaleTestRule;

@RunWith(AndroidJUnit4.class)
public class ScreenshotGrabber {

    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    @Before
    public void setup() {
        ActivityScenario.launch(EntryEditActivity.class);
    }

    @Test
    public void testTakeScreenshot() {
        Screengrab.screenshot("before_button_click");
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());
        Screengrab.screenshot("after_button_click");
    }

    @Test
    public void example() {
        Assert.assertTrue(true);
    }
}
