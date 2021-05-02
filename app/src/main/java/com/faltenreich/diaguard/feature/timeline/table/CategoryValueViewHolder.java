package com.faltenreich.diaguard.feature.timeline.table;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemTableCategoryValueBinding;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.apache.commons.lang3.StringUtils;

public class CategoryValueViewHolder extends BaseViewHolder<ListItemTableCategoryValueBinding, CategoryValueListItem> {

    CategoryValueViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_table_category_value);
        getBinding().valueLabel.setOnClickListener((view) -> showUnit());
    }

    @Override
    protected ListItemTableCategoryValueBinding createBinding(View view) {
        return ListItemTableCategoryValueBinding.bind(view);
    }

    @Override
    public void onBind(CategoryValueListItem item) {
        TextView valueLabel = getBinding().valueLabel;
        String value = item.print();

        valueLabel.setLines(1);

        int lines = StringUtils.countMatches(value, "\n") + 1;
        valueLabel.setLines(lines);

        if (value.length() > 0) {
            valueLabel.setText(value);
            valueLabel.setClickable(true);
        } else {
            valueLabel.setText(null);
            valueLabel.setClickable(false);
        }
    }

    private void showUnit() {
        Category category = getItem().getCategory();
        String unitAcronym = PreferenceStore.getInstance().getUnitName(category);
        ViewUtils.showToast(getContext(), unitAcronym);
    }
}
