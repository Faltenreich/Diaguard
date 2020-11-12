package com.faltenreich.diaguard.feature.preference.factor;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityFactorBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class FactorActivity extends BaseActivity<ActivityFactorBinding> {

    public FactorActivity() {
        super(R.layout.activity_factor);
    }

    @Override
    protected ActivityFactorBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityFactorBinding.inflate(layoutInflater);
    }
}
