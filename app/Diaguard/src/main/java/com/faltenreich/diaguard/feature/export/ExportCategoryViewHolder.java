package com.faltenreich.diaguard.feature.export;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

class ExportCategoryViewHolder extends BaseViewHolder<ExportCategoryListItem> {

    @BindView(R.id.category_image) ImageView categoryImageView;
    @BindView(R.id.category_checkbox) CheckBox categoryCheckBox;
    @BindView(R.id.extra_checkbox) CheckBox extraCheckBox;

    ExportCategoryViewHolder(View view) {
        super(view);
        init();
    }

    private void init() {
        categoryCheckBox.setOnCheckedChangeListener((checkBox, isChecked) -> {
            getListItem().setCategorySelected(isChecked);
            extraCheckBox.setEnabled(isChecked);
        });
        extraCheckBox.setOnCheckedChangeListener((checkBox, isChecked) -> getListItem().setExtraSelected(isChecked));
    }

    @Override
    protected void bindData() {
        ExportCategoryListItem item = getListItem();
        Category category = item.getCategory();

        categoryImageView.setImageResource(category.getIconImageResourceId());
        categoryCheckBox.setText(getContext().getString(category.getStringResId()));
        categoryCheckBox.setChecked(item.isCategorySelected());

        String extraTitle;
        switch (category) {
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
        extraCheckBox.setText(extraTitle);
        extraCheckBox.setEnabled(item.isCategorySelected());
        extraCheckBox.setChecked(item.isExtraSelected());
        extraCheckBox.setVisibility(extraTitle != null ? View.VISIBLE : View.GONE);
    }
}
