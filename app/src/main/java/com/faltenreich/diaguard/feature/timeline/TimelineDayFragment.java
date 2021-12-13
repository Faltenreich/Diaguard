package com.faltenreich.diaguard.feature.timeline;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.databinding.FragmentTimelineDayBinding;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.timeline.chart.DayChart;
import com.faltenreich.diaguard.feature.timeline.chart.DayChartData;
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
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
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

    private static final String TAG = TimelineDayFragment.class.getSimpleName();
    private static final String EXTRA_DATE_TIME = "EXTRA_DATE_TIME";
    private static final int SKIP_EVERY_X_HOUR = 2;

    private RecyclerView imageListView;
    private RecyclerView valueListView;
    private NestedScrollView scrollView;
    private DayChart chartView;

    private NestedScrollView.OnScrollChangeListener onScrollListener;
    private CategoryImageListAdapter imageAdapter;
    private CategoryValueListAdapter valueAdapter;
    private Category[] categories;

    private TimelineDayData data;

    public static TimelineDayFragment createInstance(DateTime dateTime) {
        TimelineDayFragment fragment = new TimelineDayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE_TIME, dateTime);
        fragment.setArguments(bundle);
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

        // Delay invalidation to improve performance on instantiation
        new Handler().postDelayed(this::invalidateData, 300);
    }

    private void init() {
        DateTime day = getArguments() != null
            ? (DateTime) getArguments().getSerializable(EXTRA_DATE_TIME)
            : null;
        data = new TimelineDayData(day != null ? day : DateTime.now());
        categories = PreferenceStore.getInstance().getActiveCategories(Category.BLOODSUGAR);
        imageAdapter = new CategoryImageListAdapter(getContext());
        valueAdapter = new CategoryValueListAdapter(getContext());
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
        if (getContext() == null) {
            return;
        }

        if (data.needsChartData()) {
            Log.d(TAG, "Invalidating data for chart on " + data.getDay().toString());
            DataLoader.getInstance().load(getContext(), new DataLoaderListener<DayChartData>() {
                @Override
                public DayChartData onShouldLoad(Context context) {
                    List<Measurement> values = new ArrayList<>();
                    List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(data.getDay());
                    if (entries != null && entries.size() > 0) {
                        for (Entry entry : entries) {
                            // TODO: Improve performance by using transaction / bulk fetch
                            List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry, new Category[] { Category.BLOODSUGAR });
                            values.addAll(measurements);
                        }
                    }
                    return new DayChartData(context, PreferenceStore.getInstance().getTimelineStyle(), values);
                }

                @Override
                public void onDidLoad(DayChartData chartData) {
                    data.setChartData(chartData);
                    invalidateChart();
                }
            });
        } else {
            invalidateChart();
        }

        if (data.needsListData()) {
            Log.d(TAG, "Invalidating data for list on " + data.getDay().toString());
            DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<CategoryValueListItem>>() {
                @Override
                public List<CategoryValueListItem> onShouldLoad(Context context) {
                    List<CategoryValueListItem> listItems = new ArrayList<>();
                    LinkedHashMap<Category, CategoryValueListItem[]> values = EntryDao.getInstance().getAverageDataTable(data.getDay(), categories, SKIP_EVERY_X_HOUR);
                    for (Map.Entry<Category, CategoryValueListItem[]> mapEntry : values.entrySet()) {
                        Collections.addAll(listItems, mapEntry.getValue());
                    }
                    return listItems;
                }
                @Override
                public void onDidLoad(List<CategoryValueListItem> listData) {
                    data.setListData(listData);
                    invalidateList();
                }
            });
        } else {
            invalidateList();
        }
    }

    private void invalidateChart() {
        if (isAdded() && !data.needsChartData()) {
            Log.d(TAG, "Invalidating view for chart on " + data.getDay().toString());
            DayChartData chartData = data.getChartData();
            chartView.setData(chartData);
            chartView.getAxisLeft().setAxisMaximum(chartData.getYAxisMaximum());
            // Workaround: Fixes invalidation within ViewPager
            // https://github.com/PhilJay/MPAndroidChart/issues/1274
            // FIXME: Leads to jumping chart view
            chartView.invalidate();
        }
    }

    public void invalidateList() {
        if (isAdded() && !data.needsListData()) {
            Log.d(TAG, "Invalidating view for list on " + data.getDay().toString());
            List<CategoryValueListItem> valueListItems = data.getListData();
            if (valueAdapter.getItemCount() > 0) {
                for (int index = 0; index < valueListItems.size(); index++) {
                    CategoryValueListItem listItem = valueListItems.get(index);
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
                valueAdapter.addItems(valueListItems);
                valueAdapter.notifyDataSetChanged();
            }
        }
    }

    public DateTime getDay() {
        return data.getDay();
    }

    public void setDay(DateTime day) {
        if (data.getDay() != day) {
            data.setDay(day);
            data.reset();
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
        data.reset();
        invalidateData();
    }

    private void openEntry(Entry entry) {
        openFragment(EntryEditFragment.newInstance(entry), true);
    }
}
