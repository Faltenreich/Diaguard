package com.faltenreich.diaguard.feature.food.search;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class FoodSearchViewHolder extends BaseViewHolder<FoodSearchListItem> implements View.OnClickListener {

    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_brand) TextView brand;
    @BindView(R.id.food_carbohydrates) TextView carbohydrates;
    @BindView(R.id.food_recent) ImageView recentIndicator;

    FoodSearchViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        Food food = getListItem().getFood();
        name.setText(food.getName());
        brand.setText(food.getBrand());
        brand.setVisibility(food.getBrand() != null && food.getBrand().length() > 0 ? View.VISIBLE : View.GONE);
        carbohydrates.setText(food.getValueForUi());
        recentIndicator.setVisibility(getListItem().getFoodEaten() != null ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        Events.post(new FoodSelectedEvent(getListItem().getFood(), view));
    }
}
