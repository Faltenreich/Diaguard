package com.faltenreich.diaguard.feature.food.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentFoodSearchBinding;
import com.faltenreich.diaguard.feature.food.detail.FoodDetailFragment;
import com.faltenreich.diaguard.feature.food.edit.FoodEditFragment;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.food.FoodPreferenceFragment;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.data.repository.FoodRepository;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodQueryEndedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodQueryStartedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodSavedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodFoundEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.shared.networking.NetworkingUtils;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;
import com.faltenreich.diaguard.shared.view.recyclerview.pagination.EndlessRecyclerViewScrollListener;
import com.faltenreich.diaguard.shared.view.search.SearchView;
import com.faltenreich.diaguard.shared.view.search.SearchViewAction;
import com.faltenreich.diaguard.shared.view.search.SearchViewListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class FoodSearchFragment
    extends BaseFragment<FragmentFoodSearchBinding>
    implements ToolbarDescribing, SearchViewListener {

    public static final String FINISH_ON_SELECTION = "finishOnSelection";

    private FoodSearchListAdapter listAdapter;
    private LinearLayoutManager listLayoutManager;

    private int currentPage;
    private boolean finishOnSelection;

    public static FoodSearchFragment newInstance(boolean finishOnSelection) {
        FoodSearchFragment fragment = new FoodSearchFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(FINISH_ON_SELECTION, finishOnSelection);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static FoodSearchFragment newInstance() {
        return newInstance(false);
    }

    protected FoodSearchFragment() {
        super(R.layout.fragment_food_search);
    }

    @Override
    protected FragmentFoodSearchBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentFoodSearchBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.food)
            .setShowToolbar(false)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestArguments();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
        newSearch();
    }

    @Override
    public void onPause() {
        Events.unregister(this);
        super.onPause();
    }

    private void requestArguments() {
        if (getArguments() != null) {
            finishOnSelection = getArguments().getBoolean(FINISH_ON_SELECTION);
        }
    }

    private void initLayout() {
        getBinding().fab.setOnClickListener((view) -> createFood());
        getBinding().emptyButton.setOnClickListener((view) -> onEmptyButtonClick());

        getBinding().unitLabel.setText(PreferenceStore.getInstance().getLabelForMealPer100g(requireContext()));

        SwipeRefreshLayout swipeRefreshLayout = getBinding().foodSearchSwipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.green_light, R.color.green_lighter);
        swipeRefreshLayout.setOnRefreshListener(this::newSearch);

        SearchView searchView = getBinding().searchView;
        searchView.setSearchListener(this);
        searchView.setAction(new SearchViewAction(R.drawable.ic_more_vertical, R.string.menu_open, (view) -> openSettings()));
        searchView.setSuggestions(PreferenceStore.getInstance().getInputQueries());

        listAdapter = new FoodSearchListAdapter(getContext());
        listLayoutManager = new LinearLayoutManager(getContext());
        EndlessRecyclerViewScrollListener listScrollListener = new EndlessRecyclerViewScrollListener(listLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                continueSearch();
            }
        };

        RecyclerView listView = getBinding().listView;
        listView.setLayoutManager(listLayoutManager);
        listView.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        listView.setAdapter(listAdapter);
        listView.addOnScrollListener(listScrollListener);
    }

    private void newSearch() {
        getBinding().foodSearchSwipeRefreshLayout.setRefreshing(true);
        getBinding().emptyView.setVisibility(View.GONE);

        currentPage = 0;

        clear();
        continueSearch();
    }

    private void continueSearch() {
        FoodRepository.getInstance().search(getContext(), getBinding().searchView.getQuery(), currentPage, this::addItems);
    }

    private void addItems(List<FoodSearchListItem> items) {
        currentPage++;
        boolean hasItems = items.size() > 0;
        if (hasItems) {
            int oldSize = listAdapter.getItemCount();
            int newCount = 0;
            for (FoodSearchListItem item : items) {
                if (!listAdapter.getItems().contains(item)) {
                    listAdapter.addItem(item);
                    newCount++;
                }
            }
            listAdapter.notifyItemRangeInserted(oldSize, newCount);
        }
        Events.post(new FoodQueryEndedEvent(hasItems));
    }

    private void removeItem(Food food) {
        for (int position = 0; position < listAdapter.getItemCount(); position++) {
            FoodSearchListItem listItem = listAdapter.getItem(position);
            if (listItem.getFood().equals(food)) {
                listAdapter.removeItem(position);
                listAdapter.notifyItemRemoved(position);
                break;
            }
        }
    }

    private void clear() {
        int oldCount = listAdapter.getItemCount();
        listAdapter.clear();
        listAdapter.notifyItemRangeRemoved(0, oldCount);
    }

    private void showEmptyList() {
        if (getContext() != null) {
            if (StringUtils.isBlank(getBinding().searchView.getQuery())) {
                showError(R.drawable.ic_settings, R.string.error_no_data, R.string.error_no_data_settings_desc, R.string.settings_open);
            } else if (NetworkingUtils.isOnline(getContext())) {
                showError(R.drawable.ic_sad, R.string.error_no_data, R.string.error_no_data_desc, R.string.food_add_desc);
            } else {
                showError(R.drawable.ic_wifi, R.string.error_no_connection, R.string.error_no_connection_desc, R.string.try_again);
            }
        }
    }

    private void showError(@DrawableRes int iconResId, @StringRes int textResId, @StringRes int descResId, @StringRes int buttonTextResId) {
        getBinding().emptyView.setVisibility(View.VISIBLE);
        getBinding().emptyIcon.setImageResource(iconResId);
        getBinding().emptyTitleLabel.setText(textResId);
        getBinding().emptyDescriptionLabel.setText(descResId);
        getBinding().emptyButton.setText(buttonTextResId);
    }

    private void openSettings() {
        openFragment(new FoodPreferenceFragment(), Navigation.Operation.REPLACE, true);
    }

    private void openFood(Food food) {
        openFragment(FoodDetailFragment.newInstance(food.getId()), Navigation.Operation.REPLACE, true);
    }

    private void createFood() {
        openFragment(new FoodEditFragment(), Navigation.Operation.REPLACE, true);
    }

    private void onEmptyButtonClick() {
        if (StringUtils.isBlank(getBinding().searchView.getQuery())) {
            openSettings();
        } else {
            // Workaround since CONNECTIVITY_ACTION broadcasts cannot be caught since API level 24
            boolean wasNetworkError = getBinding().emptyTitleLabel.getText().toString().equals(getString(R.string.error_no_connection));
            if (wasNetworkError) {
                newSearch();
            } else {
                createFood();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodQueryStartedEvent event) {
        if (listAdapter.getItemCount() == 0) {
            getBinding().foodSearchSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodQueryEndedEvent event) {
        getBinding().foodSearchSwipeRefreshLayout.setRefreshing(false);
        if (listAdapter.getItemCount() == 0) {
            showEmptyList();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSavedEvent event) {
        getBinding().searchView.setQuery(null, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final FoodDeletedEvent event) {
        removeItem(event.context);
        ViewUtils.showSnackbar(getView(), getString(R.string.food_deleted), v -> {
            Food food = event.context;
            food.setDeletedAt(null);
            FoodDao.getInstance().createOrUpdate(food);
            Events.post(new FoodSavedEvent(food));
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSelectedEvent event) {
        if (finishOnSelection) {
            finish();
            Events.post(new FoodFoundEvent(event.context));
        } else {
            openFood(event.context);
        }
    }

    @Override
    public void onQueryChanged(String query) {
        PreferenceStore.getInstance().addInputQuery(query);
        newSearch();
    }

    @Override
    public void onQueryClosed() {
        finish();
    }
}
