package com.faltenreich.diaguard.ui.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.FoodAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemFood;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.ui.activity.FoodSearchActivity;

import butterknife.BindView;

/**
 * Created by Faltenreich on 11.09.2016.
 */
public class FoodSearchFragment extends BaseFragment implements SearchView.OnQueryTextListener {

    public static final String EXTRA_MODE = "EXTRA_MODE";

    public enum Mode {
        READ,
        SELECT
    }

    @BindView(R.id.food_search_list) RecyclerView list;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItemSearch = menu.findItem(R.id.action_search);
        if (menuItemSearch != null) {
            SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menuItemSearch.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextListener(this);
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

    private void initList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        adapter = new FoodAdapter(getContext());
        list.setAdapter(adapter);
    }

    private void updateList() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.clear();
        for (int i = 0; i < newText.length(); i++) {
            adapter.addItem(new ListItemFood(new Food()));
        }
        adapter.notifyDataSetChanged();
        return false;
    }
}
