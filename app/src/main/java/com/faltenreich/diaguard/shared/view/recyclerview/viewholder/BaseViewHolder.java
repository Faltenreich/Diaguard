package com.faltenreich.diaguard.shared.view.recyclerview.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public abstract class BaseViewHolder <BINDING extends ViewBinding, ITEM> extends RecyclerView.ViewHolder {

    private final BINDING binding;
    private final Context context;
    private ITEM item;

    private BaseViewHolder(View view) {
        super(view);
        context = view.getContext();
        binding = createBinding(view);
        ButterKnife.bind(this, binding.getRoot());
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int layoutResource) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false));
    }

    protected abstract BINDING createBinding(View view);

    protected BINDING getBinding() {
        return binding;
    }

    protected Context getContext() {
        return context;
    }

    protected ITEM getItem() {
        return item;
    }

    public void bind(ITEM item) {
        this.item = item;
        onBind(item);
    }

    protected abstract void onBind(ITEM item);

    public int getSwipeFlags() {
        return 0;
    }
}