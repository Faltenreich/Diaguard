package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemNutrient;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.view.viewholder.NutrientViewHolder;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class NutrientAdapter extends BaseAdapter<ListItemNutrient, NutrientViewHolder> {

    private Food food;

    public NutrientAdapter(Context context, Food food) {
        super(context);
        this.food = food;
        init();
    }

    private void init() {
        for (Food.Nutrient nutrient : Food.Nutrient.values()) {
            addItem(new ListItemNutrient(nutrient.getLabel(), nutrient.getValue(food)));
        }
        notifyItemRangeInserted(0, getItemCount());
    }

    @Override
    public NutrientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NutrientViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_nutrient, parent, false));
    }

    @Override
    public void onBindViewHolder(NutrientViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
    }
}
