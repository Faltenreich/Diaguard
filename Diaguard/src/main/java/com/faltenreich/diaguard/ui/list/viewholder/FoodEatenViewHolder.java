package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.util.Helper;

import org.joda.time.DateTime;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodEatenViewHolder extends BaseViewHolder<FoodEaten> implements View.OnClickListener {

    @BindView(R.id.list_item_food_eaten_date_time) TextView dateTime;
    @BindView(R.id.list_item_food_eaten_amount) TextView amount;

    public FoodEatenViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        FoodEaten foodEaten = getListItem();
        boolean hasDateTime = foodEaten.getMeal() != null && foodEaten.getMeal().getEntry() != null;
        if (hasDateTime) {
            DateTime foodEatenDateTime = foodEaten.getMeal().getEntry().getDate();
            dateTime.setText(String.format("%s %s",
                    Helper.getDateFormat().print(foodEatenDateTime),
                    Helper.getTimeFormat().print(foodEatenDateTime)));
        } else {
            dateTime.setText(null);
        }
        amount.setText(String.format("%s g", Helper.parseFloat(foodEaten.getAmountInGrams())));
    }

    @Override
    public void onClick(View view) {
        EntryActivity.show(getContext(), getListItem().getMeal().getEntry());
    }
}
