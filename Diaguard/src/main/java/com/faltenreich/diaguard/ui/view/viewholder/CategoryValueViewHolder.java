package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValue;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class CategoryValueViewHolder extends BaseViewHolder<ListItemCategoryValue> {

    @BindView(R.id.category_value) TextView textView;

    public CategoryValueViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        ListItemCategoryValue listItem = getListItem();
        if (listItem.getValue() > 0) {
            Measurement.Category category = listItem.getCategory();
            float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, listItem.getValue());
            String valueForUi = Helper.parseFloat(value);
            textView.setText(valueForUi);
        } else {
            textView.setText(null);
        }
    }
}
