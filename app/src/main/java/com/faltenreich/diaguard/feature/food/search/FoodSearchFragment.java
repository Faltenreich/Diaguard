package com.faltenreich.diaguard.feature.food.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentFoodSearchBinding;
import com.faltenreich.diaguard.feature.food.BaseFoodFragment;
import com.faltenreich.diaguard.feature.food.detail.FoodDetailActivity;
import com.faltenreich.diaguard.feature.food.edit.FoodEditActivity;
import com.faltenreich.diaguard.feature.preference.PreferenceActivity;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.data.repository.FoodRepository;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.FoodDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodQueryEndedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodQueryStartedEvent;
import com.faltenreich.diaguard.shared.event.data.FoodSavedEvent;
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

public class FoodSearchFragment extends BaseFragment implements SearchViewListener {

    public static final String FINISH_ON_SELECTION = "finishOnSelection";

    private FoodSearchListAdapter listAdapter;
    private LinearLayoutManager listLayoutManager;
    private EndlessRecyclerViewScrollListener listScrollListener;

    private int currentPage;
    private boolean finishOnSelection;
    
    private FragmentFoodSearchBinding binding;

    public FoodSearchFragment() {
        super(R.layout.fragment_food_search, R.string.food);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFoodSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
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

    private void init() {
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle extras = getActivity().getIntent().getExtras();
            finishOnSelection = extras.getBoolean(FINISH_ON_SELECTION);
        }

        listAdapter = new FoodSearchListAdapter(getContext());
        listLayoutManager = new LinearLayoutManager(getContext());
        listScrollListener = new EndlessRecyclerViewScrollListener(listLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                continueSearch();
            }
        };
    }

    private void initLayout() {
        binding.fab.setOnClickListener((view) -> createFood());
        binding.foodSearchEmptyButton.setOnClickListener((view) -> onEmptyButtonClick());

        binding.foodSearchUnit.setText(PreferenceStore.getInstance().getLabelForMealPer100g(requireContext()));

        binding.foodSearchSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.green_light, R.color.green_lighter);
        binding.foodSearchSwipeRefreshLayout.setOnRefreshListener(this::newSearch);

        binding.searchView.setSearchListener(this);
        binding.searchView.setAction(new SearchViewAction(R.drawable.ic_more_vertical, R.string.menu_open, (view) -> openSettings()));
        binding.searchView.setSuggestions(PreferenceStore.getInstance().getInputQueries());

        binding.foodSearchList.setLayoutManager(listLayoutManager);
        binding.foodSearchList.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        binding.foodSearchList.setAdapter(listAdapter);
        binding.foodSearchList.addOnScrollListener(listScrollListener);
    }

    private void newSearch() {
        binding.foodSearchSwipeRefreshLayout.setRefreshing(true);
        binding.foodSearchListEmpty.setVisibility(View.GONE);

        currentPage = 0;

        clear();
        continueSearch();
    }

    private void continueSearch() {
        FoodRepository.getInstance().search(getContext(), binding.searchView.getQuery(), currentPage, this::addItems);
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

    private void showSearchView() {
        if (getContext() != null) {
            if (StringUtils.isBlank(binding.searchView.getQuery())) {
                showError(R.drawable.ic_settings, R.string.error_no_data, R.string.error_no_data_settings_desc, R.string.settings_open);
            } else if (NetworkingUtils.isOnline(getContext())) {
                showError(R.drawable.ic_sad, R.string.error_no_data, R.string.error_no_data_desc, R.string.food_add_desc);
            } else {
                showError(R.drawable.ic_wifi, R.string.error_no_connection, R.string.error_no_connection_desc, R.string.try_again);
            }
        }
    }

    private void showError(@DrawableRes int iconResId, @StringRes int textResId, @StringRes int descResId, @StringRes int buttonTextResId) {
        binding.foodSearchListEmpty.setVisibility(View.VISIBLE);
        binding.foodSearchEmptyIcon.setImageResource(iconResId);
        binding.foodSearchEmptyText.setText(textResId);
        binding.foodSearchEmptyDescription.setText(descResId);
        binding.foodSearchEmptyButton.setText(buttonTextResId);
    }

    private void openSettings() {
        startActivity(PreferenceActivity.newInstance(getContext(), PreferenceActivity.Link.FOOD));
    }

    private void openFood(Food food) {
        Intent intent = new Intent(getContext(), FoodDetailActivity.class);
        intent.putExtra(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        startActivity(intent);
    }

    private void createFood() {
        startActivity(new Intent(getContext(), FoodEditActivity.class));
    }

    private void onEmptyButtonClick() {
        if (StringUtils.isBlank(binding.searchView.getQuery())) {
            openSettings();
        } else {
            // Workaround since CONNECTIVITY_ACTION broadcasts cannot be caught since API level 24
            boolean wasNetworkError = binding.foodSearchEmptyText.getText().toString().equals(getString(R.string.error_no_connection));
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
            binding.foodSearchSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodQueryEndedEvent event) {
        binding.foodSearchSwipeRefreshLayout.setRefreshing(false);
        if (listAdapter.getItemCount() == 0) {
            showSearchView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSavedEvent event) {
        binding.searchView.setQuery(null, true);
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
