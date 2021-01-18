package com.faltenreich.diaguard.feature.timeline;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.databinding.FragmentTimelineDayBinding;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragmentFactory;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.timeline.chart.DayChart;
import com.faltenreich.diaguard.feature.timeline.table.CategoryImageListAdapter;
import com.faltenreich.diaguard.feature.timeline.table.CategoryImageListItem;
import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListAdapter;
import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListItem;
import com.faltenreich.diaguard.feature.timeline.table.CategoryValueViewHolder;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.GridDividerItemDecoration;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimelineDayFragment extends BaseFragment<FragmentTimelineDayBinding> {

    private static final String EXTRA_DATE_TIME = "EXTRA_DATE_TIME";
    private static final int SKIP_EVERY_X_HOUR = 2;

    private RecyclerView imageListView;
    private RecyclerView valueListView;
    private NestedScrollView scrollView;
    private DayChart chartView;

    private DateTime day;
    private NestedScrollView.OnScrollChangeListener onScrollListener;
    private CategoryImageListAdapter imageAdapter;
    private CategoryValueListAdapter valueAdapter;
    private Category[] categories;
    private boolean isVisible;

    private List<CategoryValueListItem> temp;

    public static TimelineDayFragment createInstance(DateTime dateTime) {
        TimelineDayFragment fragment = new TimelineDayFragment();
        if (fragment.getArguments() != null) {
            fragment.getArguments().putSerializable(EXTRA_DATE_TIME, dateTime);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_DATE_TIME, dateTime);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected FragmentTimelineDayBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentTimelineDayBinding.inflate(layoutInflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();
        initLayout();
        setDay(day);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
    }

    private void init() {
        categories = PreferenceStore.getInstance().getActiveCategories(Category.BLOODSUGAR);
        imageAdapter = new CategoryImageListAdapter(getContext());
        valueAdapter = new CategoryValueListAdapter(getContext());
        if (getArguments() != null) {
            day = (DateTime) getArguments().getSerializable(EXTRA_DATE_TIME);
        }
    }

    private void bindViews() {
        imageListView = getBinding().imageListView;
        valueListView = getBinding().valueListView;
        scrollView = getBinding().scrollView;
        chartView = getBinding().chartView;
    }

    private void initLayout() {
        imageListView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        imageListView.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        imageListView.setAdapter(imageAdapter);
        imageListView.setNestedScrollingEnabled(false);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), DateTimeConstants.HOURS_PER_DAY / 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        valueListView.setLayoutManager(layoutManager);
        valueListView.addItemDecoration(new GridDividerItemDecoration(getContext()));
        valueListView.setAdapter(valueAdapter);
        valueListView.setNestedScrollingEnabled(false);

        scrollView.setOnScrollChangeListener(onScrollListener);
        chartView.setOnItemSelectedListener(this::openEntry);
    }

    private void invalidateData() {
        chartView.setDay(day);

        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<CategoryValueListItem>>() {
            @Override
            public List<CategoryValueListItem> onShouldLoad() {
                List<CategoryValueListItem> listItems = new ArrayList<>();
                LinkedHashMap<Category, CategoryValueListItem[]> values = EntryDao.getInstance().getAverageDataTable(day, categories, SKIP_EVERY_X_HOUR);
                for (Map.Entry<Category, CategoryValueListItem[]> mapEntry : values.entrySet()) {
                    Collections.addAll(listItems, mapEntry.getValue());
                }
                return listItems;
            }
            @Override
            public void onDidLoad(List<CategoryValueListItem> values) {
                temp = values;
                if (isVisible) {
                    // Update only onPageChanged to improve performance
                    invalidateLayout();
                } else if (valueAdapter.getItemCount() == 0) {
                    // Delay updating invisible fragments onStart to improve performance
                    new Handler().postDelayed(() -> invalidateLayout(), 500);
                }
            }
        });
    }

    public void invalidateLayout() {
        if (isAdded() && temp != null) {
            if (valueAdapter.getItemCount() > 0) {
                for (int index = 0; index < temp.size(); index++) {
                    CategoryValueListItem listItem = temp.get(index);
                    RecyclerView.ViewHolder viewHolder = valueListView.findViewHolderForAdapterPosition(index);
                    if (viewHolder instanceof CategoryValueViewHolder) {
                        valueAdapter.setItem(listItem, index);
                        // We access the ViewHolder directly for better performance compared to notifyItem(Range)Changed
                        CategoryValueViewHolder categoryValueViewHolder = (CategoryValueViewHolder) viewHolder;
                        categoryValueViewHolder.bind(listItem);
                    }
                }
            } else {
                for (Category category : categories) {
                    imageAdapter.addItem(new CategoryImageListItem(category));
                }
                imageAdapter.notifyDataSetChanged();

                // Other notify methods lead to rendering issues on view paging
                valueAdapter.addItems(temp);
                valueAdapter.notifyDataSetChanged();
            }
        }
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        if (isAdded()) {
            invalidateData();
        }
    }

    public void setOnScrollListener(NestedScrollView.OnScrollChangeListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void scrollTo(int yOffset) {
        if (isAdded()) {
            int y = yOffset - valueListView.computeVerticalScrollOffset();
            scrollView.scrollBy(0, y);
        }
    }

    public void reset() {
        categories = PreferenceStore.getInstance().getActiveCategories(Category.BLOODSUGAR);
        valueAdapter.clear();
        imageAdapter.clear();
        setDay(day);
    }

    private void openEntry(Entry entry) {
        openFragment(EntryEditFragmentFactory.newInstance(entry), Navigation.Operation.REPLACE, true);
    }
}
