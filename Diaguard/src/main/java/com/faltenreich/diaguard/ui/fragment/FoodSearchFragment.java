package com.faltenreich.diaguard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodAdapter;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.networking.FoodSearchFailedEvent;
import com.faltenreich.diaguard.event.networking.FoodSearchSucceededEvent;
import com.faltenreich.diaguard.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.ui.activity.FoodActivity;
import com.faltenreich.diaguard.ui.activity.FoodEditActivity;
import com.faltenreich.diaguard.ui.activity.FoodSearchActivity;
import com.faltenreich.diaguard.ui.view.FoodRecyclerView;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodSearchFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnMenuClickListener {

    public static final String EXTRA_MODE = "EXTRA_MODE";

    public enum Mode {
        READ,
        SELECT
    }

    @BindView(R.id.food_search_query) TextView queryTextView;
    @BindView(R.id.food_search_unit) TextView unitTextView;
    @BindView(R.id.food_search_list) FoodRecyclerView list;
    @BindView(R.id.food_search_progress) MaterialProgressBar progressBar;
    @BindView(R.id.search_view) SearchView searchView;

    @BindView(R.id.food_search_list_empty) ViewGroup emptyList;
    @BindView(R.id.food_search_empty_icon) ImageView emptyIcon;
    @BindView(R.id.food_search_empty_text) TextView emptyText;
    @BindView(R.id.food_search_empty_description) TextView emptyDescription;

    private Mode mode;
    private FoodAdapter adapter;
    private String query;

    public FoodSearchFragment() {
        super(R.layout.fragment_food_search, R.string.food);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mode = Mode.READ;
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
        if (getActivity() instanceof FoodSearchActivity && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras.get(EXTRA_MODE) != null && extras.get(EXTRA_MODE) instanceof Mode) {
                this.mode = (Mode) extras.get(EXTRA_MODE);
            }
        }
    }

    private void init() {
        searchView.setOnQueryTextListener(this);
        searchView.setOnMenuClickListener(this);
        searchView.setHint(R.string.food_search);

        final List<Food> foodList = FoodDao.getInstance().getAll();
        List<SearchItem> suggestions  = new ArrayList<>();
        for (Food food : foodList) {
            suggestions.add(new SearchItem(food.getName()));
        }
        final SearchAdapter searchAdapter = new SearchAdapter(getContext(), suggestions);
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String query = textView.getText().toString();
                searchView.setQuery(query);
                searchView.close(true);
            }
        });
        searchView.setAdapter(searchAdapter);

        unitTextView.setText(PreferenceHelper.getInstance().getLabelForMealPer100g());
    }

    private void showError(@DrawableRes int iconResId, @StringRes int textResId, @StringRes int descResId) {
        emptyList.setVisibility(View.VISIBLE);
        emptyIcon.setImageResource(iconResId);
        emptyText.setText(textResId);
        emptyDescription.setText(descResId);
    }

    private void onError() {
        showError(R.drawable.ic_wifi_off, R.string.error_no_connection, R.string.error_no_connection_desc);
    }

    private void onFoodSelected(Food food) {
        if (mode == Mode.SELECT) {
            finish();
        } else if (mode == Mode.READ) {
            openFood(food);
        }
    }

    private void openFood(Food food) {
        Events.unregister(this);

        Intent intent = new Intent(getContext(), FoodActivity.class);
        intent.putExtra(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.close(true);
        list.search(query);
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

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    public void createFood() {
        startActivity(new Intent(getContext(), FoodEditActivity.class));
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodSelectedEvent event) {
        onFoodSelected(event.context);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodSearchSucceededEvent event) {
        progressBar.setVisibility(View.GONE);
    }

    @SuppressWarnings("unused")
    public void onEvent(FoodSearchFailedEvent event) {
        progressBar.setVisibility(View.GONE);
    }
}
