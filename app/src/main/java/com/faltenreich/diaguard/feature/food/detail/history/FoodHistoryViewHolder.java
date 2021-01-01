package com.faltenreich.diaguard.feature.food.detail.history;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemFoodEatenBinding;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditIntentFactory;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

class FoodHistoryViewHolder extends BaseViewHolder<ListItemFoodEatenBinding, FoodEaten> {

    FoodHistoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_food_eaten);
        itemView.setOnClickListener((view) -> openEntry());
    }

    @Override
    protected ListItemFoodEatenBinding createBinding(View view) {
        return ListItemFoodEatenBinding.bind(view);
    }

    @Override
    protected void onBind(FoodEaten item) {
        boolean hasDateTime = item.getMeal() != null && item.getMeal().getEntry() != null;
        if (hasDateTime) {
            DateTime foodEatenDateTime = item.getMeal().getEntry().getDate();
            getBinding().dateLabel.setText(String.format("%s %s",
                    Helper.getDateFormat().print(foodEatenDateTime),
                    Helper.getTimeFormat().print(foodEatenDateTime)));
        } else {
            getBinding().dateLabel.setText(null);
        }
        getBinding().amountLabel.setText(String.format("%s g", FloatUtils.parseFloat(item.getAmountInGrams())));
    }

    private void openEntry() {
        Entry entry = getItem().getMeal().getEntry();
        Intent intent = EntryEditIntentFactory.newInstance(getContext(), entry);
        getContext().startActivity(intent);
    }
}
