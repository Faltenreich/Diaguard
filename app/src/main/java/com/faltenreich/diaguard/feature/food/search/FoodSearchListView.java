package com.faltenreich.diaguard.feature.food.search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodQueryEndedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodQueryStartedEvent;
import com.faltenreich.diaguard.shared.event.networking.FoodSearchFailedEvent;
import com.faltenreich.diaguard.shared.event.networking.FoodSearchSucceededEvent;
import com.faltenreich.diaguard.feature.food.networking.OpenFoodFactsService;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.shared.view.recyclerview.pagination.EndlessRecyclerViewScrollListener;
import com.faltenreich.diaguard.shared.Helper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 10.11.2016.
 */

public class FoodSearchListView extends RecyclerView {

    private FoodSearchListAdapter adapter;
    private String query;

    private int offlinePage;
    private int onlinePage;

    public FoodSearchListView(Context context) {
        super(context);
        init();
    }

    public FoodSearchListView(Context context, AttributeSet attrs) {
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

        addItemDecoration(new LinearDividerItemDecoration(getContext()));

        adapter = new FoodSearchListAdapter(getContext());
        setAdapter(adapter);

        EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                search();
            }
        };
        addOnScrollListener(listener);
    }

    int getItemCount() {
        return adapter.getItemCount();
    }

    void clear() {
        int oldCount = adapter.getItemCount();
        adapter.clear();
        adapter.notifyItemRangeRemoved(0, oldCount);
    }

    void newSearch(String query) {
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
        OpenFoodFactsService.getInstance().search(query, onlinePage);
    }

    private void addItems(List<FoodSearchListItem> foodList) {
        boolean showBrandedFood = PreferenceHelper.getInstance().showBrandedFood();
        List<FoodSearchListItem> filtered = new ArrayList<>();
        if (!showBrandedFood) {
            for (FoodSearchListItem listItem : foodList) {
                if (!listItem.getFood().isBrandedFood()) {
                    filtered.add(listItem);
                }
            }
        } else {
            filtered = foodList;
        }

        boolean hasItems = filtered.size() > 0;
        if (hasItems) {
            int oldSize = adapter.getItemCount();
            int newCount = 0;
            for (FoodSearchListItem listItem : filtered) {
                if (!adapter.getItems().contains(listItem)) {
                    adapter.addItem(listItem);
                    newCount++;
                }
            }
            adapter.notifyItemRangeInserted(oldSize, newCount);
        }
        Events.post(new FoodQueryEndedEvent(hasItems));
    }

    // TODO: Filter on database-level
    private void addFood(List<Food> foodList) {
        List<FoodSearchListItem> foodItemList = new ArrayList<>();

        for (Food food : foodList) {
            boolean isSameLanguage = Helper.isSystemLocale(food.getLanguageCode());
            boolean isNotDeleted = !food.isDeleted();
            if (isSameLanguage && isNotDeleted) {
                foodItemList.add(new FoodSearchListItem(food));
            }
        }

        boolean skipResponse = foodList.size() > 0 && foodItemList.size() == 0;
        if (skipResponse) {
            searchOnline();
        } else {
            addItems(foodItemList);
        }
    }

    private void removeItem(Food food) {
        for (int position = 0; position < getItemCount(); position++) {
            FoodSearchListItem listItem = adapter.getItem(position);
            if (listItem.getFood().equals(food)) {
                adapter.removeItem(position);
                adapter.notifyItemRemoved(position);
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSearchSucceededEvent event) {
        onlinePage++;
        addFood(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSearchFailedEvent event) {
        Events.post(new FoodQueryEndedEvent(false));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodDeletedEvent event) {
        removeItem(event.context);
    }

    // TODO: Make static or extract into separate file
    private class LoadDataTask extends AsyncTask<Void, Void, List<FoodSearchListItem>> {

        @Override
        protected List<FoodSearchListItem> doInBackground(Void... voids) {
            List<FoodSearchListItem> foodList = new ArrayList<>();
            boolean isInitial = adapter.getItemCount() == 0 && !(query != null && query.length() > 0);

            if (isInitial) {
                List<FoodEaten> foodEatenList = FoodEatenDao.getInstance().getAllOrdered();
                for (FoodEaten foodEaten : foodEatenList) {
                    FoodSearchListItem listItem = new FoodSearchListItem(foodEaten);
                    if (!foodList.contains(listItem)) {
                        foodList.add(listItem);
                    }
                }
            }

            List<Food> foodAllList = FoodDao.getInstance().search(query, offlinePage);
            for (Food food : foodAllList) {
                // Skip food that has been eaten before
                FoodSearchListItem listItem = new FoodSearchListItem(food);
                if (!foodList.contains(listItem)) {
                    foodList.add(listItem);
                }
            }

            return foodList;
        }

        @Override
        protected void onPostExecute(List<FoodSearchListItem> foodList) {
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
