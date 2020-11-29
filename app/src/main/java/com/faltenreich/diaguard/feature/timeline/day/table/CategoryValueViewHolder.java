package com.faltenreich.diaguard.feature.timeline.day.table;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemTableCategoryValueBinding;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;

public class CategoryValueViewHolder extends BaseViewHolder<ListItemTableCategoryValueBinding, CategoryValueListItem> {

    @BindView(R.id.category_value) TextView valueView;

    CategoryValueViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_table_category_value);
        valueView.setOnClickListener((view) -> showUnit());
    }

    @Override
    protected ListItemTableCategoryValueBinding createBinding(View view) {
        return ListItemTableCategoryValueBinding.bind(view);
    }

    @Override
    public void onBind(CategoryValueListItem item) {
        String value = item.print();

        valueView.setLines(1);

        int lines = StringUtils.countMatches(value, "\n") + 1;
        valueView.setLines(lines);

        if (value.length() > 0) {
            valueView.setText(value);
            valueView.setClickable(true);
        } else {
            valueView.setText(null);
            valueView.setClickable(false);
        }
    }

    private void showUnit() {
        // TODO: Use Measurement.toString() instead
        Category category = getItem().getCategory();
        String unitAcronym = PreferenceStore.getInstance().getUnitName(category);
        ViewUtils.showToast(getContext(), unitAcronym);
    }
}
