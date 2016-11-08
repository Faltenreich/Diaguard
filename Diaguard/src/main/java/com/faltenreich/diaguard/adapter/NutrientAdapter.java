package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemNutrient;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.view.viewholder.NutrientViewHolder;
import com.faltenreich.diaguard.util.Helper;

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
            String label = nutrient.getLabel();
            Float number = nutrient.getValue(food);
            String value = getContext().getString(R.string.placeholder);
            if (number != null && number >= 0) {
                value = String.format("%s %s",
                        Helper.parseFloat(number),
                        getContext().getString(nutrient.getUnit()));
                if (nutrient == Food.Nutrient.ENERGY) {
                    value = String.format("%s %s (%s)",
                            Helper.parseFloat(Helper.parseKcalToKj(number)),
                            getContext().getString(R.string.energy_acronym_2),
                            value);
                }
            }
            addItem(new ListItemNutrient(label, value));
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
