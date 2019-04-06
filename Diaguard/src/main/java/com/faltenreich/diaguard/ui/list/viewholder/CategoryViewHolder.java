package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.adapter.CategoryListAdapter;
import com.faltenreich.diaguard.ui.list.helper.Draggable;
import com.faltenreich.diaguard.util.ResourceUtils;

import butterknife.BindView;

public class CategoryViewHolder extends BaseViewHolder<Measurement.Category> implements Draggable {

    @BindView(R.id.background) ViewGroup background;
    @BindView(R.id.titleLabel) TextView titleLabel;
    @BindView(R.id.checkBoxActive) CheckBox activeCheckBox;
    @BindView(R.id.checkBoxPinned) CheckBox pinnedCheckBox;
    @BindView(R.id.dragView) View dragView;

    public CategoryViewHolder(View view, CategoryListAdapter.Callback listener) {
        super(view);
        activeCheckBox.setOnCheckedChangeListener((v, isChecked) -> {
            PreferenceHelper.getInstance().setCategoryActive(getListItem(), isChecked);
            listener.onCheckedChange();
        });
        pinnedCheckBox.setOnCheckedChangeListener((v, isChecked) -> PreferenceHelper.getInstance().setCategoryPinned(getListItem(), isChecked));
        dragView.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                listener.onReorderStart(CategoryViewHolder.this);
                v.performClick();
            }
            return false;
        });
    }

    @Override
    protected void bindData() {
        Measurement.Category category = getListItem();
        titleLabel.setText(category.toLocalizedString(getContext()));
        activeCheckBox.setEnabled(category.isOptional());
        activeCheckBox.setChecked(PreferenceHelper.getInstance().isCategoryActive(category));
        pinnedCheckBox.setChecked(PreferenceHelper.getInstance().isCategoryPinned(category));
        dragView.setVisibility(category.isOptional() ? View.VISIBLE : View.INVISIBLE);
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
