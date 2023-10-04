package com.faltenreich.diaguard.test.junit.rule;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import com.faltenreich.diaguard.R;

public class FragmentRule<T extends Fragment> extends TestRule {

    private final Class<T> clazz;
    private FragmentScenario<T> scenario;

    public FragmentRule(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void applyBeforeTest() {
        scenario = FragmentScenario.launchInContainer(clazz, null, R.style.AppTheme, Lifecycle.State.STARTED);
    }

    @Override
    public void applyAfterTest() {
        scenario.close();
    }
}
