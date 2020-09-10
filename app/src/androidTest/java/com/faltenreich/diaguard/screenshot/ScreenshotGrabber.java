package com.faltenreich.diaguard.screenshot;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.faltenreich.diaguard.feature.navigation.MainActivity;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.locale.LocaleTestRule;

@RunWith(AndroidJUnit4.class)
public class ScreenshotGrabber {

    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    @Rule
    public ActivityTestRule<MainActivity> rule;

    @Before
    public void setup() {
        rule = new ActivityTestRule<>(MainActivity.class, false, false);
    }

    @Test
    public void testTakeScreenshot() {
        rule.launchActivity(null);
        Screengrab.screenshot("test");
    }
}