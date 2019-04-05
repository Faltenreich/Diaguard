package com.faltenreich.diaguard.ui.list.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.list.item.ListItemNutrient;
import com.faltenreich.diaguard.ui.list.viewholder.NutrientViewHolder;

public class NutrientAdapter extends BaseAdapter<ListItemNutrient, NutrientViewHolder> {

    public NutrientAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public NutrientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NutrientViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_nutrient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NutrientViewHolder holder, int position) {
        holder.bindData(getItem(holder.getAdapterPosition()));
    }
}
