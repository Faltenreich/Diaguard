package com.faltenreich.diaguard.test.junit.rule;

import android.Manifest;

import androidx.test.platform.app.InstrumentationRegistry;

import com.faltenreich.diaguard.BuildConfig;

public class GrantRuntimePermissions extends TestRule {

    @Override
    public void applyBeforeTest() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().grantRuntimePermission(
            BuildConfig.APPLICATION_ID,
            Manifest.permission.POST_NOTIFICATIONS);
    }

    @Override
    public void applyAfterTest() {

    }
}
