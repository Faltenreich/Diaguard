package com.faltenreich.diaguard.ui.view.viewholder;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValues;
import com.faltenreich.diaguard.adapter.list.ListItemEmpty;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.activity.PreferenceActivity;
import com.faltenreich.diaguard.ui.view.TintImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class TableCategoryHolder extends BaseViewHolder<ListItemCategoryValues> {

    @Bind(R.id.list_item_chart_table_image)
    protected TintImageView imageView;

    @Bind(R.id.list_item_chart_table_content)
    public ViewGroup content;

    public TableCategoryHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        ListItemCategoryValues listItem = getListItem();
        Measurement.Category category = listItem.getCategory();
        int categoryImageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(category);
        imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), categoryImageResourceId));

        for (float value : listItem.getValues()) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.list_item_table_category_value, content, false);
            if (value > 0) {
                value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, value);
                String valueForUi = PreferenceHelper.getInstance().getDecimalFormat(category).format(value);
                textView.setText(valueForUi);
            }
            content.addView(textView);
        }
    }
}
