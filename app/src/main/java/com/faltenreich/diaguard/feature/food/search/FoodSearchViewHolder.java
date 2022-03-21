package com.faltenreich.diaguard.feature.food.search;

import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemFoodSearchBinding;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodType;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
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
        FoodType foodType = food.getFoodType(getContext());
        getBinding().nameLabel.setText(food.getName());
        getBinding().brandLabel.setText(food.getBrand());
        getBinding().brandLabel.setVisibility(StringUtils.isBlank(food.getBrand()) ? View.GONE : View.VISIBLE);
        getBinding().valueLabel.setText(food.getValueForUi());
        getBinding().recentIndicator.setVisibility(item.getFoodEaten() != null ? View.VISIBLE : View.GONE);
        getBinding().typeIcon.setImageResource(foodType.getIconResource());
        getBinding().typeIcon.setOnClickListener((view) -> ViewUtils.showToast(getContext(), foodType.getLabelResource()));
    }

    private void selectFood() {
        Events.post(new FoodSelectedEvent(getItem().getFood()));
    }
}
