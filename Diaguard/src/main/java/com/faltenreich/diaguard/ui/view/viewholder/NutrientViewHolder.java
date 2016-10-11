package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemNutrient;
import com.faltenreich.diaguard.util.Helper;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class NutrientViewHolder extends BaseViewHolder<ListItemNutrient> {

    @BindView(R.id.list_item_nutrient_label) TextView label;
    @BindView(R.id.list_item_nutrient_value) TextView value;

    public NutrientViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        ListItemNutrient listItem = getListItem();
        label.setText(listItem.getLabel());
        value.setText(Helper.parseFloat(listItem.getValue()));

        boolean hasBackgroundColor = getAdapterPosition() % 2 == 0;
        //setBackgroundColor(hasBackgroundColor ? R.color.light : android.R.color.transparent);
    }
}
