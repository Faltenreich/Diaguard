package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.ui.activity.FoodSearchActivity;
import com.faltenreich.diaguard.util.Helper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodViewHolder extends BaseViewHolder<Food> implements View.OnClickListener {

    @BindView(R.id.food_image) ImageView image;
    @BindView(R.id.food_name) TextView name;
    @BindView(R.id.food_brand) TextView brand;
    @BindView(R.id.food_carbohydrates) TextView carbohydrates;

    public FoodViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        Food food = getListItem();
        Picasso.with(getContext()).load(food.getImageUrl()).into(image);
        name.setText(food.getName());
        brand.setText(food.getBrand());
        carbohydrates.setText(Helper.parseFloat(food.getCarbohydrates()));
    }

    @Override
    public void onClick(View view) {
        if (getContext() instanceof FoodSearchActivity) {
            FoodSearchActivity activity = (FoodSearchActivity) getContext();
            Events.post(new FoodSelectedEvent(getListItem()));
            activity.finish();

        /*
            Intent intent = new Intent(getContext(), FoodActivity.class);
            intent.putExtra(FoodFragment.EXTRA_FOOD_ID, getListItem().getId());
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity,
                            view,
                            "transitionFood");
            activity.startActivity(intent, options);
        */
        }
    }
}
