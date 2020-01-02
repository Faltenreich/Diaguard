package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.item.ListItemExportCategory;

import butterknife.BindView;

public class ExportCategoryViewHolder extends BaseViewHolder<ListItemExportCategory> {

    @BindView(R.id.category_image) ImageView categoryImageView;
    @BindView(R.id.category_checkbox) CheckBox categoryCheckBox;
    @BindView(R.id.extra_checkbox) CheckBox extraCheckBox;

    public ExportCategoryViewHolder(View view) {
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
        ListItemExportCategory item = getListItem();
        Measurement.Category category = item.getCategory();

        categoryImageView.setImageResource(PreferenceHelper.getInstance().getCategoryImageResourceId(category));
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
