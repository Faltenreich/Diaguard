package com.faltenreich.diaguard.feature.food.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.faltenreich.diaguard.feature.navigation.FabDescribing;
import com.faltenreich.diaguard.feature.navigation.FabDescription;
import com.faltenreich.diaguard.feature.navigation.FabProperties;
import com.faltenreich.diaguard.feature.navigation.SearchOwner;
import com.faltenreich.diaguard.feature.navigation.SearchProperties;
import com.faltenreich.diaguard.feature.navigation.Searching;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.food.FoodPreferenceFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.data.repository.FoodRepository;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodSavedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodSearchedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodSelectedEvent;
import com.faltenreich.diaguard.shared.networking.NetworkingUtils;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;
import com.faltenreich.diaguard.shared.view.recyclerview.pagination.EndlessRecyclerViewScrollListener;
import com.faltenreich.diaguard.shared.view.search.SearchViewAction;
import com.faltenreich.diaguard.shared.view.search.SearchViewListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class FoodSearchFragment
    extends BaseFragment<FragmentFoodSearchBinding>
    implements ToolbarDescribing, FabDescribing, Searching, SearchViewListener {

    public static final String FINISH_ON_SELECTION = "finishOnSelection";

    private TextView unitLabel;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView listView;
    private FoodSearchListAdapter listAdapter;
    private LinearLayoutManager listLayoutManager;

    private ViewGroup emptyView;
    private ImageView emptyIcon;
    private TextView emptyTitleLabel;
    private TextView emptyDescriptionLabel;
    private Button emptyButton;

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

    @Override
    protected FragmentFoodSearchBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentFoodSearchBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder().build();
    }

    @Override
    public SearchProperties getSearchProperties() {
        return new SearchProperties.Builder(this)
            .setHint(getString(R.string.food_search))
            .setAction(new SearchViewAction(R.drawable.ic_more_vertical, R.string.menu_open, (view) -> openSettings()))
            .setSuggestions(PreferenceStore.getInstance().getInputQueries())
            .build();
    }

    @Override
    public FabDescription getFabDescription() {
        return new FabDescription(FabProperties.addButton((view) -> createFood()), true);
    }

    @Override
    @Nullable
    public SearchOwner getSearchOwner() {
        return (SearchOwner) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestArguments();
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isRecreated = listView != null;

        bindViews();
        initLayout();

        if (!isRecreated && getSearchOwner() != null) {
            getSearchOwner().setSearchQuery(null, false);
            newSearch();
        } else {
            invalidateEmptyView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
    }

    @Override
    public void onPause() {
        Events.unregister(this);
        super.onPause();
    }

    private void requestArguments() {
        finishOnSelection = getArguments() != null && getArguments().getBoolean(FINISH_ON_SELECTION);
    }

    private void init() {
        listAdapter = new FoodSearchListAdapter(getContext());
    }

    private void bindViews() {
        unitLabel = getBinding().unitLabel;
        swipeRefreshLayout = getBinding().swipeRefreshLayout;
        listView = getBinding().listView;
        emptyView = getBinding().emptyView;
        emptyIcon = getBinding().emptyIcon;
        emptyTitleLabel = getBinding().emptyTitleLabel;
        emptyDescriptionLabel = getBinding().emptyDescriptionLabel;
        emptyButton = getBinding().emptyButton;
    }

    private void initLayout() {
        emptyButton.setOnClickListener((view) -> onEmptyButtonClick());
        unitLabel.setText(PreferenceStore.getInstance().getLabelForMealPer100g(requireContext()));

        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.green_light, R.color.green_lighter);
        swipeRefreshLayout.setOnRefreshListener(this::newSearch);

        listLayoutManager = new LinearLayoutManager(getContext());
        EndlessRecyclerViewScrollListener listScrollListener = new EndlessRecyclerViewScrollListener(listLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                continueSearch();
            }
        };

        listView.setLayoutManager(listLayoutManager);
        listView.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        listView.setAdapter(listAdapter);
        listView.addOnScrollListener(listScrollListener);
    }

    private void invalidateEmptyView() {
        if (listAdapter.getItemCount() == 0) {
            showEmptyList();
        } else {
            hideEmptyList();
        }
    }

    private void newSearch() {
        swipeRefreshLayout.setRefreshing(true);
        emptyView.setVisibility(View.GONE);

        currentPage = 0;

        clear();
        continueSearch();
    }

    private void continueSearch() {
        if (!isAdded() || getSearchOwner() == null) {
            return;
        }
        String query = getSearchOwner().getSearchQuery();
        FoodRepository.getInstance().search(getContext(), query, currentPage, this::addItems);
    }

    private void addItems(List<FoodSearchListItem> items) {
        if (!isAdded()) {
            return;
        }
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

            currentPage++;
            continueSearch();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            invalidateEmptyView();
        }
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
        if (getContext() == null || getSearchOwner() == null) {
            return;
        }
        if (StringUtils.isBlank(getSearchOwner().getSearchQuery())) {
            showError(R.drawable.ic_settings, R.string.error_no_data, R.string.error_no_data_settings_desc, R.string.settings_open);
        } else if (NetworkingUtils.isOnline(getContext())) {
            showError(R.drawable.ic_sad, R.string.error_no_data, R.string.error_no_data_desc, R.string.food_add_desc);
        } else {
            showError(R.drawable.ic_wifi, R.string.error_no_connection, R.string.error_no_connection_desc, R.string.try_again);
        }
    }

    private void hideEmptyList() {
        emptyView.setVisibility(View.GONE);
    }

    private void showError(@DrawableRes int iconResId, @StringRes int textResId, @StringRes int descResId, @StringRes int buttonTextResId) {
        emptyView.setVisibility(View.VISIBLE);
        emptyIcon.setImageResource(iconResId);
        emptyTitleLabel.setText(textResId);
        emptyDescriptionLabel.setText(descResId);
        emptyButton.setText(buttonTextResId);
    }

    private void openSettings() {
        openFragment(new FoodPreferenceFragment(), true);
    }

    private void openFood(Food food) {
        openFragment(FoodDetailFragment.newInstance(food.getId()), true);
    }

    private void createFood() {
        openFragment(new FoodEditFragment(), true);
    }

    private void onEmptyButtonClick() {
        String query = getSearchOwner() != null ? getSearchOwner().getSearchQuery() : null;
        if (StringUtils.isBlank(query)) {
            openSettings();
        } else {
            // Workaround since CONNECTIVITY_ACTION broadcasts cannot be caught since API level 24
            boolean wasNetworkError = emptyTitleLabel.getText().toString().equals(getString(R.string.error_no_connection));
            if (wasNetworkError) {
                newSearch();
            } else {
                createFood();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSavedEvent event) {
        if (getSearchOwner() != null) {
            getSearchOwner().setSearchQuery(null, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final FoodDeletedEvent event) {
        removeItem(event.context);
        ViewUtils.showSnackbar(getView(), getString(R.string.food_deleted), v -> {
            Food food = event.context;
            food.setDeletedAt(null);
            FoodRepository.getInstance().createOrUpdate(food);
            Events.post(new FoodSavedEvent(food));
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSelectedEvent event) {
        if (finishOnSelection) {
            finish();
            Events.post(new FoodSearchedEvent(event.context));
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
