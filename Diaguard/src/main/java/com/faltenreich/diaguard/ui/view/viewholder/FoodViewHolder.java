package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodViewHolder extends BaseViewHolder<Food> {

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.food_name) TextView name;

    public FoodViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        Food food = getListItem();
        Picasso.with(getContext()).load("http://static.openfoodfacts.org/images/products/073/762/806/4502/front_en.6.200.jpg").into(image);
        name.setText(food.getName());
    }
}
