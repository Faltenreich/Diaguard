package com.faltenreich.diaguard.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.event.data.FoodQueryEndedEvent;
import com.faltenreich.diaguard.event.data.FoodQueryStartedEvent;
import com.faltenreich.diaguard.event.data.FoodSavedEvent;
import com.faltenreich.diaguard.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.ui.activity.FoodActivity;
import com.faltenreich.diaguard.ui.activity.FoodEditActivity;
import com.faltenreich.diaguard.ui.view.FoodRecyclerView;
import com.faltenreich.diaguard.util.NetworkingUtils;
import com.faltenreich.diaguard.util.ViewHelper;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.faltenreich.diaguard.R.id.food_search_list_empty;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodSearchFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnMenuClickListener {

    public static final String FINISH_ON_SELECTION = "finishOnSelection";

    @BindView(R.id.food_search_unit) TextView unitTextView;
    @BindView(R.id.food_search_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.food_search_list) FoodRecyclerView list;
    @BindView(R.id.search_view) SearchView searchView;

    @BindView(food_search_list_empty) ViewGroup emptyList;
    @BindView(R.id.food_search_empty_icon) ImageView emptyIcon;
    @BindView(R.id.food_search_empty_text) TextView emptyText;
    @BindView(R.id.food_search_empty_description) TextView emptyDescription;
    @BindView(R.id.food_search_empty_button) Button emptyButton;

    private boolean finishOnSelection;
    private SearchAdapter searchAdapter;

    public FoodSearchFragment() {
        super(R.layout.fragment_food_search, R.string.food);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        checkIntents();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Events.unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.food_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                searchView.open(true);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkIntents() {
        if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            finishOnSelection = extras.getBoolean(FINISH_ON_SELECTION);
        }
    }

    private void init() {
        searchView.setOnQueryTextListener(this);
        searchView.setOnMenuClickListener(this);
        searchView.setHint(R.string.food_search);
        searchView.setArrowOnly(false);

        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.green_light, R.color.green_lighter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                query(searchView.getQuery().toString());
            }
        });

        // TODO: Road to barcode scanner
        /*
        LinearLayout layout = (LinearLayout) searchView.findViewById(R.id.linearLayout);

        TintImageView cameraIcon = new TintImageView(getContext());
        cameraIcon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int vertical = getResources().getDimensionPixelSize(R.dimen.padding_large);
        int horizontal = getResources().getDimensionPixelSize(R.dimen.padding);
        cameraIcon.setPadding(horizontal, vertical, horizontal, vertical);

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        cameraIcon.setBackgroundResource(typedValue.resourceId);

        cameraIcon.setImageResource(R.drawable.ic_camera);
        cameraIcon.setTintColor(ContextCompat.getColor(getContext(), R.color.gray_darker));
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanner();
            }
        });
        layout.addView(cameraIcon, layout.getChildCount() - 1);
        */

        searchAdapter = new SearchAdapter(getContext());
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String query = textView.getText().toString();
                searchView.setQuery(query, true);
                searchView.close(true);
            }
        });
        searchView.setAdapter(searchAdapter);
        new SetSuggestionsTask().execute();

        unitTextView.setText(PreferenceHelper.getInstance().getLabelForMealPer100g());
    }

    private void showError(@DrawableRes int iconResId, @StringRes int textResId, @StringRes int descResId, @StringRes int buttonTextResId) {
        emptyList.setVisibility(View.VISIBLE);
        emptyIcon.setImageResource(iconResId);
        emptyText.setText(textResId);
        emptyDescription.setText(descResId);
        emptyButton.setText(buttonTextResId);
    }

    private void onFoodSelected(Food food) {
        if (finishOnSelection) {
            finish();
        } else {
            openFood(food);
        }
    }

    private void openFood(Food food) {
        Events.unregister(this);

        Intent intent = new Intent(getContext(), FoodActivity.class);
        intent.putExtra(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        startActivity(intent);
    }
    private void query(String query) {
        emptyList.setVisibility(View.GONE);
        list.newSearch(query);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.close(true);
        PreferenceHelper.getInstance().addInputQuery(query);
        query(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onMenuClick() {
        if (searchView.isSearchOpen()) {
            searchView.close(true);
        } else {
            finish();
        }
    }

    private void createFood() {
        startActivity(new Intent(getContext(), FoodEditActivity.class));
    }

    private void showEmptyList() {
        if (NetworkingUtils.isOnline()) {
            showError(R.drawable.ic_sad, R.string.error_no_data, R.string.error_no_data_desc, R.string.food_add);
        } else {
            showError(R.drawable.ic_wifi, R.string.error_no_connection, R.string.error_no_connection_desc, R.string.try_again);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    public void onFabClick() {
        createFood();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.food_search_empty_button)
    public void onEmptyButtonClick() {
        // Workaround since CONNECTIVITY_ACTION broadcasts cannot be caught since API level 24
        boolean wasNetworkError = emptyText.getText().toString().equals(getString(R.string.error_no_connection));
        if (wasNetworkError) {
            query(searchView.getQuery().toString());
        } else {
            createFood();
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.imageView_clear)
    public void clearQuery() {
        searchView.setTextOnly(null);
        searchView.close(true);
        query(null);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(FoodSelectedEvent event) {
        onFoodSelected(event.context);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(FoodQueryStartedEvent event) {
        swipeRefreshLayout.setRefreshing(true);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(FoodQueryEndedEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        if (list.getItemCount() == 0) {
            showEmptyList();
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(FoodSavedEvent event) {
        clearQuery();
    }

    @SuppressWarnings("unused")
    public void onEvent(final FoodDeletedEvent event) {
        ViewHelper.showSnackbar(getView(), getString(R.string.food_deleted), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = event.context;
                FoodDao.getInstance().createOrUpdate(food);
                Events.post(new FoodSavedEvent(food));
            }
        });
    }

    private class SetSuggestionsTask extends AsyncTask<Void, Void, ArrayList<ArrayList<SearchItem>>> {

        @Override
        protected ArrayList<ArrayList<SearchItem>> doInBackground(Void... voids) {
            ArrayList<ArrayList<SearchItem>> searchItems = new ArrayList<>();

            ArrayList<SearchItem> suggestions = new ArrayList<>();
            for (Food food : FoodDao.getInstance().getAll()) {
                suggestions.add(new SearchItem(food.getName()));
            }
            searchItems.add(suggestions);

            ArrayList<SearchItem> recent = new ArrayList<>();
            for (String recentQuery : PreferenceHelper.getInstance().getInputQueries()) {
                recent.add(new SearchItem(recentQuery));
            }
            searchItems.add(recent);

            return searchItems;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<SearchItem>> searchItems) {
            super.onPostExecute(searchItems);

            searchAdapter.setSuggestionsList(searchItems.get(0));
            searchAdapter.setData(searchItems.get(1));
        }
    }
}
