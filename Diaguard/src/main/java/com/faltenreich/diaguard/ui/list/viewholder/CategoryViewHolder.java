package com.faltenreich.diaguard.ui.list.viewholder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.helper.Selectable;
import com.faltenreich.diaguard.util.ResourceUtils;
import com.faltenreich.diaguard.util.ViewUtils;
import com.faltenreich.diaguard.util.theme.ThemeUtils;
import com.google.android.material.internal.ContextUtils;

import androidx.core.graphics.ColorUtils;
import butterknife.BindView;

public class CategoryViewHolder extends BaseViewHolder<Measurement.Category> implements Selectable {

    @BindView(R.id.background) ViewGroup background;
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

    @Override
    public void setSelected(boolean isSelected) {
        background.setBackgroundColor(isSelected ? ResourceUtils.getBackgroundSecondary(getContext()) : ResourceUtils.getBackgroundPrimary(getContext()));
    }
}
