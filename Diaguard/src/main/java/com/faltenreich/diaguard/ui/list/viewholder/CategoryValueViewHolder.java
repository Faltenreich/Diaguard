package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.util.ViewUtils;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;

public class CategoryValueViewHolder extends BaseViewHolder<ListItemCategoryValue> implements View.OnClickListener {

    @BindView(R.id.category_value) TextView valueView;

    public CategoryValueViewHolder(View view) {
        super(view);
        valueView.setOnClickListener(this);
    }

    @Override
    public void bindData() {
        valueView.setLines(1);
        ListItemCategoryValue listItem = getListItem();
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
        Measurement.Category category = getListItem().getCategory();
        String unitAcronym = PreferenceHelper.getInstance().getUnitName(category);
        ViewUtils.showToast(getContext(), unitAcronym);
    }
}
