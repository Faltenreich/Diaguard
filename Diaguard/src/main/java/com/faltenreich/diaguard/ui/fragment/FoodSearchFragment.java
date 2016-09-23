package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodAdapter;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.activity.FoodSearchActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodSearchFragment extends BaseFragment implements MaterialSearchView.OnQueryTextListener {

    public static final String EXTRA_MODE = "EXTRA_MODE";

    public enum Mode {
        READ,
        SELECT
    }

    @BindView(R.id.food_search_list) RecyclerView list;
    @BindView(R.id.search_view) MaterialSearchView searchView;

    private Mode mode;
    private FoodAdapter adapter;

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
        initList();
        initSearch();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
        updateSearch();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.food, menu);
        super.onCreateOptionsMenu(menu, inflater);
        searchView.setMenuItem(menu.findItem(R.id.action_search));
    }

    private void checkIntents() {
        if (getActivity() instanceof FoodSearchActivity && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras.get(EXTRA_MODE) != null && extras.get(EXTRA_MODE) instanceof Mode) {
                this.mode = (Mode) extras.get(EXTRA_MODE);
            }
        }
    }

    private void initList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        adapter = new FoodAdapter(getContext());
        Food food = new Food();
        food.setName("Rice");
        adapter.addItem(food);
        list.setAdapter(adapter);
    }

    private void initSearch() {
        searchView.setVoiceSearch(true);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
    }

    private void updateList() {
        List<Food> foodList = FoodDao.getInstance().getAll();
        adapter.clear();
        adapter.addItems(foodList);
        adapter.notifyDataSetChanged();
    }

    private void updateSearch() {
        if (adapter.getItemCount() > 0) {
            String[] suggestions = new String[adapter.getItemCount()];
            for (int position = 0; position < adapter.getItemCount(); position++) {
                Food food = adapter.getItem(position);
                suggestions[position] = food.getName();
            }
            searchView.setSuggestions(suggestions);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
