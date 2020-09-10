package com.faltenreich.diaguard.screenshot;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.feature.navigation.MainActivity;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.locale.LocaleTestRule;

@RunWith(AndroidJUnit4.class)
public class ScreenshotGrabber {

    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    @Test
    public void testTakeScreenshot() {
        ActivityScenario.launch(MainActivity.class);
        Screengrab.screenshot("test");
    }
}