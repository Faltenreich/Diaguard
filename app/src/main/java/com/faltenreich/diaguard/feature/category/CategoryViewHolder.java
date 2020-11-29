package com.faltenreich.diaguard.feature.category;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemCategoryBinding;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.view.recyclerview.drag.Draggable;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;

import butterknife.BindView;

class CategoryViewHolder extends BaseViewHolder<ListItemCategoryBinding, Category> implements Draggable {

    @BindView(R.id.background) ViewGroup background;
    @BindView(R.id.titleLabel) TextView titleLabel;
    @BindView(R.id.checkBoxActive) CheckBox activeCheckBox;
    @BindView(R.id.checkBoxPinned) CheckBox pinnedCheckBox;
    @BindView(R.id.dragView) View dragView;

    private final CategoryListAdapter.Listener listener;

    CategoryViewHolder(ViewGroup parent, CategoryListAdapter.Listener listener) {
        super(parent, R.layout.list_item_category);
        this.listener = listener;

        activeCheckBox.setOnCheckedChangeListener((view, isChecked) -> setActive(isChecked));
        pinnedCheckBox.setOnCheckedChangeListener((view, isChecked) -> setPinned(isChecked));
        dragView.setOnTouchListener((view, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                listener.onReorderStart(CategoryViewHolder.this);
                view.performClick();
            }
            return false;
        });
    }

    @Override
    protected ListItemCategoryBinding createBinding(View view) {
        return ListItemCategoryBinding.bind(view);
    }

    @Override
    protected void onBind(Category item) {
        titleLabel.setText(getContext().getString(item.getStringResId()));
        activeCheckBox.setEnabled(item.isOptional());
        activeCheckBox.setChecked(PreferenceStore.getInstance().isCategoryActive(item));
        pinnedCheckBox.setChecked(PreferenceStore.getInstance().isCategoryPinned(item));
        dragView.setVisibility(item.isOptional() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean isDraggable() {
        return getItem() != Category.BLOODSUGAR;
    }

    @Override
    public void onDrag(boolean isDragged) {
        background.setBackgroundColor(isDragged ? ColorUtils.getBackgroundSecondary(getContext()) : ColorUtils.getBackgroundPrimary(getContext()));
    }

    private void setActive(boolean isActive) {
        PreferenceStore.getInstance().setCategoryActive(getItem(), isActive);
        listener.onCheckedChange();
    }

    private void setPinned(boolean isPinned) {
        PreferenceStore.getInstance().setCategoryPinned(getItem(), isPinned);
    }
}
