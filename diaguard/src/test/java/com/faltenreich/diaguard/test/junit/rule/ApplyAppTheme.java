package com.faltenreich.diaguard.test.junit.rule;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.faltenreich.diaguard.R;

public class ApplyAppTheme extends TestRule {

    @Override
    public void applyBeforeTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.setTheme(R.style.AppTheme);
    }

    @Override
    public void applyAfterTest() {

    }
}
