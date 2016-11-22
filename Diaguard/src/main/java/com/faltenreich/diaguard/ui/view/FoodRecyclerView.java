package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.faltenreich.diaguard.adapter.EndlessRecyclerViewScrollListener;
import com.faltenreich.diaguard.adapter.FoodAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.adapter.list.ListItemFood;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.FoodQueryEndedEvent;
import com.faltenreich.diaguard.event.data.FoodQueryStartedEvent;
import com.faltenreich.diaguard.event.networking.FoodSearchFailedEvent;
import com.faltenreich.diaguard.event.networking.FoodSearchSucceededEvent;
import com.faltenreich.diaguard.networking.openfoodfacts.OpenFoodFactsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 10.11.2016.
 */

public class FoodRecyclerView extends RecyclerView {

    private FoodAdapter adapter;
    private String query;

    private int offlinePage;
    private int onlinePage;

    public FoodRecyclerView(Context context) {
        super(context);
        init();
    }

    public FoodRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Events.register(this);
        newSearch(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Events.unregister(this);
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

        addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        adapter = new FoodAdapter(getContext());
        setAdapter(adapter);

        EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                search();
            }
        };
        addOnScrollListener(listener);
    }

    public int getItemCount() {
        return adapter.getItemCount();
    }

    public void clear() {
        int oldCount = adapter.getItemCount();
        adapter.clear();
        adapter.notifyItemRangeRemoved(0, oldCount);
    }

    public void newSearch(String query) {
        this.query = query;
        this.offlinePage = 0;
        this.onlinePage = 0;

        clear();
        search();
    }

    private void search() {
        // offlinePage gets invalid after online searches
        boolean isSearchOnline = onlinePage > 0;
        if (isSearchOnline) {
            searchOnline();
        } else {
            searchOffline();
        }
    }

    private void searchOffline() {
        Events.post(new FoodQueryStartedEvent());
        new LoadDataTask().execute();
    }

    private void searchOnline() {
        Events.post(new FoodQueryStartedEvent());
        OpenFoodFactsManager.getInstance().search(query, onlinePage);
    }

    private void addItems(List<ListItemFood> foodList) {
        boolean hasItems = foodList.size() > 0;
        if (hasItems) {
            int oldSize = adapter.getItemCount();
            adapter.addItems(foodList);
            adapter.notifyItemRangeInserted(oldSize, oldSize + foodList.size());
        }
        Events.post(new FoodQueryEndedEvent(hasItems));
    }

    private void addFood(List<Food> foodList) {
        List<ListItemFood> foodItemList = new ArrayList<>();
        for (Food food : foodList) {
            foodItemList.add(new ListItemFood(food));
        }
        addItems(foodItemList);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(FoodSearchSucceededEvent event) {
        onlinePage++;
        addFood(event.context);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(FoodSearchFailedEvent event) {
        Events.post(new FoodQueryEndedEvent(false));
    }

    private class LoadDataTask extends AsyncTask<Void, Void, List<ListItemFood>> {

        @Override
        protected List<ListItemFood> doInBackground(Void... voids) {
            List<ListItemFood> foodList = new ArrayList<>();
            boolean isInitial = adapter.getItemCount() == 0 && !(query != null && query.length() > 0);

            if (isInitial) {
                List<FoodEaten> foodEatenList = FoodEatenDao.getInstance().getAllOrdered();
                for (FoodEaten foodEaten : foodEatenList) {
                    ListItemFood listItem = new ListItemFood(foodEaten);
                    if (!foodList.contains(listItem)) {
                        foodList.add(listItem);
                    }
                }
            }

            List<Food> foodAllList = FoodDao.getInstance().search(query, offlinePage);
            for (Food food : foodAllList) {
                // Skip food that has been eaten before
                ListItemFood listItem = new ListItemFood(food);
                if (!foodList.contains(listItem)) {
                    foodList.add(listItem);
                }
            }

            return foodList;
        }

        @Override
        protected void onPostExecute(List<ListItemFood> foodList) {
            super.onPostExecute(foodList);

            if (foodList.size() > 0) {
                offlinePage++;
                addItems(foodList);

            } else {
                searchOnline();
            }
        }
    }
}
