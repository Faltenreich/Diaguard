package com.faltenreich.diaguard.feature.food.search;

import android.view.View;
import android.view.ViewGroup;
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
class FoodSearchViewHolder extends BaseViewHolder<FoodSearchListItem> {

    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_brand) TextView brand;
    @BindView(R.id.food_carbohydrates) TextView carbohydrates;
    @BindView(R.id.food_recent) ImageView recentIndicator;

    FoodSearchViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_food_search);
        itemView.setOnClickListener((view) -> selectFood());
    }

    @Override
    protected void onBind(FoodSearchListItem item) {
        Food food = item.getFood();
        name.setText(food.getName());
        brand.setText(food.getBrand());
        brand.setVisibility(food.getBrand() != null && food.getBrand().length() > 0 ? View.VISIBLE : View.GONE);
        carbohydrates.setText(food.getValueForUi());
        recentIndicator.setVisibility(item.getFoodEaten() != null ? View.VISIBLE : View.GONE);
    }

    private void selectFood() {
        Events.post(new FoodSelectedEvent(getItem().getFood(), itemView));
    }
}
