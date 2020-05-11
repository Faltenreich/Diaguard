package com.faltenreich.diaguard.feature.food.detail.nutrient;

import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class NutrientViewHolder extends BaseViewHolder<NutrientListItem> {

    @BindView(R.id.list_item_nutrient_label) TextView label;
    @BindView(R.id.list_item_nutrient_value) TextView value;

    NutrientViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_nutrient);
    }

    @Override
    protected void bind() {
        NutrientListItem listItem = getItem();
        label.setText(listItem.getLabel());
        value.setText(listItem.getValue());
    }
}
