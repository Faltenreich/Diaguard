package com.faltenreich.diaguard.feature.category;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemCategoryBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.view.recyclerview.drag.Draggable;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;

class CategoryViewHolder extends BaseViewHolder<ListItemCategoryBinding, Category> implements Draggable {

    private final CategoryListAdapter.Listener listener;

    CategoryViewHolder(ViewGroup parent, CategoryListAdapter.Listener listener) {
        super(parent, R.layout.list_item_category);
        this.listener = listener;
        initLayout();
    }

    @Override
    protected ListItemCategoryBinding createBinding(View view) {
        return ListItemCategoryBinding.bind(view);
    }

    private void initLayout() {
        getBinding().activeCheckbox.setOnCheckedChangeListener((view, isChecked) -> setActive(isChecked));
        getBinding().pinnedCheckbox.setOnCheckedChangeListener((view, isChecked) -> setPinned(isChecked));
        getBinding().dragIndicator.setOnTouchListener((view, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                listener.onReorderStart(CategoryViewHolder.this);
                view.performClick();
            }
            return false;
        });
    }

    @Override
    protected void onBind(Category item) {
        getBinding().titleLabel.setText(getContext().getString(item.getStringResId()));
        getBinding().activeCheckbox.setEnabled(item.isOptional());
        getBinding().activeCheckbox.setChecked(PreferenceStore.getInstance().isCategoryActive(item));
        getBinding().pinnedCheckbox.setChecked(PreferenceStore.getInstance().isCategoryPinned(item));
        getBinding().dragIndicator.setVisibility(item.isOptional() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean isDraggable() {
        return getItem() != Category.BLOODSUGAR;
    }

    @Override
    public void onDrag(boolean isDragged) {
        getBinding().container.setBackgroundColor(isDragged
            ? ColorUtils.getBackgroundSecondary(getContext())
            : ColorUtils.getBackgroundPrimary(getContext()));
    }

    private void setActive(boolean isActive) {
        PreferenceStore.getInstance().setCategoryActive(getItem(), isActive);
        listener.onCheckedChange();
    }

    private void setPinned(boolean isPinned) {
        PreferenceStore.getInstance().setCategoryPinned(getItem(), isPinned);
    }
}
