package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.CheckBox;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;

import butterknife.BindView;

public class CategoryViewHolder extends BaseViewHolder<Measurement.Category> {

    @BindView(R.id.checkbox) CheckBox checkBox;

    public CategoryViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        Measurement.Category category = getListItem();
        checkBox.setEnabled(category != Measurement.Category.BLOODSUGAR);
        checkBox.setText(category.toLocalizedString(getContext()));
        checkBox.setChecked(PreferenceHelper.getInstance().isCategoryActive(category));
        checkBox.setOnCheckedChangeListener((view, isChecked) -> PreferenceHelper.getInstance().setIsCategoryActive(category, isChecked));
    }
}
