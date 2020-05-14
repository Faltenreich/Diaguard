package com.faltenreich.diaguard.test.junit.rule;

import androidx.test.platform.app.InstrumentationRegistry;

import com.faltenreich.diaguard.feature.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.Database;

public class CleanUpData extends TestRule {

    @Override
    public void applyBeforeTest() {
        PreferenceHelper.getInstance().init(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @Override
    public void applyAfterTest() {
        Database.getInstance().close();
    }
}
