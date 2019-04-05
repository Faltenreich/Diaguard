package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.helper.Draggable;
import com.faltenreich.diaguard.util.ResourceUtils;
import com.faltenreich.diaguard.util.ViewUtils;

import butterknife.BindView;

public class CategoryViewHolder extends BaseViewHolder<Measurement.Category> implements Draggable {

    @BindView(R.id.background) ViewGroup background;
    @BindView(R.id.checkBox) CheckBox checkBox;
    @BindView(R.id.dragView) View dragView;

    public CategoryViewHolder(View view) {
        super(view);
        checkBox.setOnCheckedChangeListener((v, isChecked) -> PreferenceHelper.getInstance().setIsCategoryActive(getListItem(), isChecked));
        dragView.setOnClickListener(v -> ViewUtils.showToast(getContext(), R.string.drag_drop_hint));
    }

    @Override
    protected void bindData() {
        Measurement.Category category = getListItem();
        checkBox.setEnabled(category.isOptional());
        checkBox.setText(category.toLocalizedString(getContext()));
        checkBox.setChecked(PreferenceHelper.getInstance().isCategoryActive(category));
        dragView.setVisibility(category.isOptional() ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean isDraggable() {
        return getListItem() != Measurement.Category.BLOODSUGAR;
    }

    @Override
    public void onDrag(boolean isDragged) {
        background.setBackgroundColor(isDragged ? ResourceUtils.getBackgroundSecondary(getContext()) : ResourceUtils.getBackgroundPrimary(getContext()));
    }
}
