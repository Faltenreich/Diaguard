package com.faltenreich.diaguard.feature.category;

import android.view.LayoutInflater;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityCategoriesBinding;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class CategoryListActivity extends BaseActivity<ActivityCategoriesBinding> {

    public CategoryListActivity() {
        super(R.layout.activity_categories);
    }

    @Override
    protected ActivityCategoriesBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityCategoriesBinding.inflate(layoutInflater);
    }
}