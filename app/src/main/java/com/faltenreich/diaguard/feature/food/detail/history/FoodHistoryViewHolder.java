package com.faltenreich.diaguard.feature.food.detail.history;

import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class FoodHistoryViewHolder extends BaseViewHolder<FoodEaten> {

    @BindView(R.id.list_item_food_eaten_date_time) TextView dateTime;
    @BindView(R.id.list_item_food_eaten_amount) TextView amount;

    FoodHistoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_food_eaten);
        itemView.setOnClickListener((view) -> openEntry());
    }

    @Override
    protected void onBind(FoodEaten item) {
        boolean hasDateTime = item.getMeal() != null && item.getMeal().getEntry() != null;
        if (hasDateTime) {
            DateTime foodEatenDateTime = item.getMeal().getEntry().getDate();
            dateTime.setText(String.format("%s %s",
                    Helper.getDateFormat().print(foodEatenDateTime),
                    Helper.getTimeFormat().print(foodEatenDateTime)));
        } else {
            dateTime.setText(null);
        }
        amount.setText(String.format("%s g", FloatUtils.parseFloat(item.getAmountInGrams())));
    }

    private void openEntry() {
        EntryEditActivity.show(getContext(), getItem().getMeal().getEntry());
    }
}
