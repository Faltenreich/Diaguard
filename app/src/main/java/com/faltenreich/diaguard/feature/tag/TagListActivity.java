package com.faltenreich.diaguard.feature.tag;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityTagsBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class TagListActivity extends BaseActivity<ActivityTagsBinding> {

    public TagListActivity() {
        super(R.layout.activity_tags);
    }

    @Override
    protected ActivityTagsBinding getBinding(LayoutInflater layoutInflater) {
        return ActivityTagsBinding.inflate(layoutInflater);
    }
}