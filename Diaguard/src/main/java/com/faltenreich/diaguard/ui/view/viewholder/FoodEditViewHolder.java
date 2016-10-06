package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;

import butterknife.BindView;

/**
 * Created by Faltenreich on 03.10.2016.
 */

public class FoodEditViewHolder extends BaseViewHolder<Food> implements RangeBar.OnRangeBarChangeListener {

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_amount_in_grams) RangeBar amount;

    public FoodEditViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        amount.setOnRangeBarChangeListener(this);
    }

    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

    }
}
