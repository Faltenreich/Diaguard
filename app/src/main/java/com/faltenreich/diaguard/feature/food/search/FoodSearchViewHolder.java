package com.faltenreich.diaguard.feature.food.search;

import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemFoodSearchBinding;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

/**
 * Created by Faltenreich on 11.09.2016.
 */
class FoodSearchViewHolder extends BaseViewHolder<ListItemFoodSearchBinding, FoodSearchListItem> {

    FoodSearchViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_food_search);
        itemView.setOnClickListener((view) -> selectFood());
    }

    @Override
    protected ListItemFoodSearchBinding createBinding(View view) {
        return ListItemFoodSearchBinding.bind(view);
    }

    @Override
    protected void onBind(FoodSearchListItem item) {
        Food food = item.getFood();
        getBinding().nameLabel.setText(food.getName());
        getBinding().brandLabel.setText(food.getBrand());
        getBinding().brandLabel.setVisibility(food.getBrand() != null && food.getBrand().length() > 0 ? View.VISIBLE : View.GONE);
        getBinding().valueLabel.setText(food.getValueForUi());
        getBinding().recentIndicator.setVisibility(item.getFoodEaten() != null ? View.VISIBLE : View.GONE);
    }

    private void selectFood() {
        Events.post(new FoodSelectedEvent(getItem().getFood(), itemView));
    }
}
