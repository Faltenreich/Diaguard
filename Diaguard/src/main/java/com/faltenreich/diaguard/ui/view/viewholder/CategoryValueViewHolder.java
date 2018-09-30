package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValue;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.ViewUtils;

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
        Measurement.Category category = listItem.getCategory();
        StringBuilder stringBuilder = new StringBuilder();
        if (listItem.getValueOne() > 0) {
            float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, listItem.getValueOne());
            stringBuilder.append(Helper.parseFloat(value));
        }
        if (listItem.getValueTwo() > 0) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n");
                valueView.setLines(2);
            }
            float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, listItem.getValueTwo());
            stringBuilder.append(Helper.parseFloat(value));
        }
        if (stringBuilder.length() > 0) {
            valueView.setText(stringBuilder.toString());
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
