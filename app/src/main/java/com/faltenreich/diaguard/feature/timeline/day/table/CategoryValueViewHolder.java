package com.faltenreich.diaguard.feature.timeline.day.table;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;
import com.faltenreich.diaguard.shared.view.ViewUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;

public class CategoryValueViewHolder extends BaseViewHolder<CategoryValueListItem> implements View.OnClickListener {

    @BindView(R.id.category_value) TextView valueView;

    CategoryValueViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_table_category_value);
        valueView.setOnClickListener(this);
    }

    @Override
    public void bind() {
        valueView.setLines(1);
        CategoryValueListItem listItem = getItem();
        String value = listItem.print();

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

    @Override
    public void onClick(View view) {
        // TODO: Use Measurement.toString() instead
        Category category = getItem().getCategory();
        String unitAcronym = PreferenceHelper.getInstance().getUnitName(category);
        ViewUtils.showToast(getContext(), unitAcronym);
    }
}
