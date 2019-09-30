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
    }

    @Override
    protected void bindData() {
        ListItemExportCategory item = getListItem();
        Measurement.Category category = item.getCategory();
        categoryImageView.setImageResource(PreferenceHelper.getInstance().getCategoryImageResourceId(category));
        categoryCheckBox.setText(category.toLocalizedString(getContext()));
        categoryCheckBox.setChecked(item.isCategorySelected());
        extraCheckBox.setChecked(item.isExtraSelected());
    }
}
