package com.faltenreich.diaguard.feature.export;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemExportCategoryBinding;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

class ExportCategoryViewHolder extends BaseViewHolder<ListItemExportCategoryBinding, ExportCategoryListItem> {

    ExportCategoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_export_category);
        initCategoryCheckbox();
        initExtraCheckbox();
    }

    @Override
    protected ListItemExportCategoryBinding createBinding(View view) {
        return ListItemExportCategoryBinding.bind(view);
    }

    @Override
    protected void onBind(ExportCategoryListItem item) {
        invalidateImageView();
        invalidateCategoryCheckbox();
        invalidateExtraCheckbox();
    }

    private void invalidateImageView() {
        ImageView imageView = getBinding().categoryImageView;
        imageView.setImageResource(getItem().getCategory().getIconImageResourceId());
    }

    private void initCategoryCheckbox() {
        CheckBox checkbox = getBinding().categoryCheckbox;
        checkbox.setOnCheckedChangeListener((checkBox, isChecked) -> {
            getItem().setCategorySelected(isChecked);
            getBinding().extraCheckbox.setEnabled(isChecked);
        });
    }

    private void invalidateCategoryCheckbox() {
        CheckBox checkbox = getBinding().categoryCheckbox;
        checkbox.setText(getContext().getString(getItem().getCategory().getStringResId()));
        checkbox.setChecked(getItem().isCategorySelected());
    }

    private void initExtraCheckbox() {
        CheckBox checkbox = getBinding().extraCheckbox;
        checkbox.setOnCheckedChangeListener((checkBox, isChecked) ->
            getItem().setExtraSelected(isChecked)
        );
    }

    private void invalidateExtraCheckbox() {
        CheckBox checkbox = getBinding().extraCheckbox;
        String extraTitle;
        switch (getItem().getCategory()) {
            case BLOODSUGAR:
                extraTitle = getContext().getString(R.string.highlight_limits);
                break;
            case INSULIN:
                extraTitle = getContext().getString(R.string.insulin_split);
                break;
            case MEAL:
                extraTitle = getContext().getString(R.string.food_append);
                break;
            default:
                extraTitle = null;
        }
        checkbox.setText(extraTitle);
        checkbox.setEnabled(getItem().isCategorySelected());
        checkbox.setChecked(getItem().isExtraSelected());
        checkbox.setVisibility(extraTitle != null ? View.VISIBLE : View.GONE);
    }
}
