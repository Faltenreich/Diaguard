package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.util.Helper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodViewHolder extends BaseViewHolder<Food> {

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_ingredients) TextView ingredients;

    public FoodViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        Food food = getListItem();
        Picasso.with(getContext()).load(food.getImageUrl()).into(image);
        name.setText(food.getName());
        ingredients.setText(String.format("%s %s",
                Helper.parseFloat(food.getCarbohydrates()),
                getContext().getResources().getStringArray(R.array.meal_units_acronyms)[0]));
    }
}
