package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.CheckBox;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ViewUtils;
import com.google.android.material.internal.ContextUtils;

import butterknife.BindView;

public class CategoryViewHolder extends BaseViewHolder<Measurement.Category> {

    @BindView(R.id.checkBox) CheckBox checkBox;
    @BindView(R.id.dragView) View dragView;

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
        dragView.setOnClickListener(view -> ViewUtils.showToast(getContext(), R.string.drag_drop_hint));
    }
}
