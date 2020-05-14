package com.faltenreich.diaguard.test.junit.rule;

import androidx.test.platform.app.InstrumentationRegistry;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.Database;

public class CleanUpData extends TestRule {

    @Override
    public void applyBeforeTest() {
        PreferenceStore.getInstance().init(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    @Override
    public void applyAfterTest() {
        Database.getInstance().close();
    }
}
