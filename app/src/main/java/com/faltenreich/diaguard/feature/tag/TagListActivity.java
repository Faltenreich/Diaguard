package com.faltenreich.diaguard.feature.tag;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.databinding.ActivityTagsBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class TagListActivity extends BaseActivity<ActivityTagsBinding> {

    @Override
    protected ActivityTagsBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityTagsBinding.inflate(layoutInflater);
    }
}