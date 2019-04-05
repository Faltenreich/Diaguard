package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.faltenreich.diaguard.ui.list.helper.EndlessRecyclerViewScrollListener;
import com.faltenreich.diaguard.ui.list.adapter.FoodAdapter;
import com.faltenreich.diaguard.ui.list.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.ui.list.item.ListItemFood;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.event.Events;
import com.faltenreich.diaguard.data.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.data.event.data.FoodQueryEndedEvent;
import com.faltenreich.diaguard.data.event.data.FoodQueryStartedEvent;
import com.faltenreich.diaguard.data.event.networking.FoodSearchFailedEvent;
import com.faltenreich.diaguard.data.event.networking.FoodSearchSucceededEvent;
import com.faltenreich.diaguard.networking.openfoodfacts.OpenFoodFactsService;
import com.faltenreich.diaguard.util.Helper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 10.11.2016.
 */

public class FoodListView extends RecyclerView {

    private FoodAdapter adapter;
    private String query;

    private int offlinePage;
    private int onlinePage;

    public FoodListView(Context context) {
        super(context);
        init();
    }

    public FoodListView(Context context, AttributeSet attrs) {
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
        OpenFoodFactsService.getInstance().search(query, onlinePage);
    }

    private void addItems(List<ListItemFood> foodList) {
        boolean showBrandedFood = PreferenceHelper.getInstance().showBrandedFood();
        List<ListItemFood> filtered = new ArrayList<>();
        if (!showBrandedFood) {
            for (ListItemFood listItem : foodList) {
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
            for (ListItemFood listItem : filtered) {
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
        List<ListItemFood> foodItemList = new ArrayList<>();

        for (Food food : foodList) {
            boolean isSameLanguage = Helper.isSystemLocale(food.getLanguageCode());
            boolean isNotDeleted = !food.isDeleted();
            if (isSameLanguage && isNotDeleted) {
                foodItemList.add(new ListItemFood(food));
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
            ListItemFood listItem = adapter.getItem(position);
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
