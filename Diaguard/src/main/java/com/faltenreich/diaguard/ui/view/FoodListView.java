package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodEditableAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.ui.FoodEatenRemovedEvent;
import com.faltenreich.diaguard.event.ui.FoodEatenUpdatedEvent;
import com.faltenreich.diaguard.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.ui.activity.FoodSearchActivity;
import com.faltenreich.diaguard.ui.fragment.FoodSearchFragment;
import com.j256.ormlite.dao.ForeignCollection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Faltenreich on 13.10.2016.
 */

public class FoodListView extends LinearLayout {

    @BindView(R.id.food_list_separator) View separator;
    @BindView(R.id.food_list) RecyclerView foodList;

    private FoodEditableAdapter adapter;
    private OnContentChangeListener listener;

    private Meal meal;

    public FoodListView(Context context) {
        super(context);
        init();
    }

    public FoodListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FoodListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Events.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Events.unregister(this);
    }

    public void setOnContentChangeListener(OnContentChangeListener listener) {
        this.listener = listener;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_food_list, this);
        ButterKnife.bind(this);

        meal = new Meal();

        adapter = new FoodEditableAdapter(getContext());
        foodList.setLayoutManager(new LinearLayoutManager(getContext()));
        foodList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        foodList.setAdapter(adapter);

        update();
    }

    private void update() {
        boolean hasFood = adapter.getItemCount() > 0;
        separator.setVisibility(hasFood ? VISIBLE : GONE);
        if (listener != null) {
            listener.onContentChanged();
        }
    }

    public List<FoodEaten> getItems() {
        return adapter.getItems();
    }

    public void addItem(FoodEaten foodEaten) {
        adapter.addItem(foodEaten);
        adapter.notifyItemInserted(this.adapter.getItemCount() - 1);
        update();
    }

    public void addItem(Food food) {
        FoodEaten foodEaten = new FoodEaten();
        foodEaten.setFood(food);
        foodEaten.setMeal(meal);
        addItem(foodEaten);
    }

    public void addItems(ForeignCollection<FoodEaten> foodEatenList) {
        int oldCount = adapter.getItemCount();
        for (FoodEaten foodEaten : foodEatenList) {
            adapter.addItem(foodEaten);
        }
        adapter.notifyItemRangeInserted(oldCount, adapter.getItemCount());
        update();
    }

    public void removeItem(int position) {
        adapter.removeItem(position);
        adapter.notifyItemRemoved(position);
        update();
    }

    public void updateItem(FoodEaten foodEaten, int position) {
        adapter.updateItem(position, foodEaten);
        adapter.notifyItemChanged(position);
        update();
    }

    public float getTotalCarbohydrates() {
        return adapter.getTotalCarbohydrates();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.food_list_button)
    public void searchForFood() {
        Intent intent = new Intent(getContext(), FoodSearchActivity.class);
        intent.putExtra(FoodSearchFragment.EXTRA_MODE, FoodSearchFragment.Mode.SELECT);
        getContext().startActivity(intent);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodSelectedEvent event) {
        addItem(event.context);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodEatenUpdatedEvent event) {
        updateItem(event.context, event.position);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodEatenRemovedEvent event) {
        removeItem(event.position);
    }

    public interface OnContentChangeListener {
        void onContentChanged();
    }
}
