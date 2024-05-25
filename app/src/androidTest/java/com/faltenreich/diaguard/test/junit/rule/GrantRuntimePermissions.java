package com.faltenreich.diaguard.test.junit.rule;

import android.Manifest;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;

import com.faltenreich.diaguard.BuildConfig;

public class GrantRuntimePermissions extends TestRule {

    @Override
    public void applyBeforeTest() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            InstrumentationRegistry.getInstrumentation().getUiAutomation().grantRuntimePermission(
                BuildConfig.APPLICATION_ID,
                Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    @Override
    public void applyAfterTest() {

    }
}
