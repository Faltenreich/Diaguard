package com.faltenreich.diaguard.ui.view.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemFood;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.FoodSelectedEvent;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodViewHolder extends BaseViewHolder<ListItemFood> implements View.OnClickListener {

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.food_image_placeholder) TextView placeholder;
    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_brand) TextView brand;
    @BindView(R.id.food_carbohydrates) TextView carbohydrates;
    @BindView(R.id.food_recent) ImageView recentIndicator;

    public FoodViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        Food food = getListItem().getFood();
        placeholder.setText(food.getName() != null && food.getName().length() > 0 ?
                food.getName().substring(0, 1).toUpperCase() :
                null);
        if (!TextUtils.isEmpty(food.getImageUrl())) {
            Picasso.with(getContext()).load(food.getImageUrl()).fit().centerCrop().into(image);
        } else {
            image.setImageResource(0);
        }
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
