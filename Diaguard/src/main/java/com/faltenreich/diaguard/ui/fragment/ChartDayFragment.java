package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.async.DataLoader;
import com.faltenreich.diaguard.data.async.DataLoaderListener;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.adapter.CategoryImageListAdapter;
import com.faltenreich.diaguard.ui.list.adapter.CategoryValueListAdapter;
import com.faltenreich.diaguard.ui.list.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.ui.list.decoration.TimelineGridDividerItemDecoration;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryImage;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.ui.list.viewholder.CategoryValueViewHolder;
import com.faltenreich.diaguard.ui.view.chart.DayChart;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDayFragment extends Fragment {
    private static final String EXTRA_DATE_TIME = "EXTRA_DATE_TIME";
    private static final int SKIP_EVERY_X_HOUR = 2;

    @BindView(R.id.day_chart) DayChart dayChart;
    @BindView(R.id.scroll_view) NestedScrollView scrollView;
    // TODO: Merge both lists into one with pimped GridLayoutManager
    @BindView(R.id.category_table_images) RecyclerView imageTable;
    @BindView(R.id.category_table_values) RecyclerView valueTable;

    private DateTime day;
    private NestedScrollView.OnScrollChangeListener onScrollListener;
    private CategoryImageListAdapter imageAdapter;
    private CategoryValueListAdapter valueAdapter;
    private Measurement.Category[] categories;
    private boolean isVisible;

    private List<ListItemCategoryValue> temp;

    public static ChartDayFragment createInstance(DateTime dateTime) {
        ChartDayFragment fragment = new ChartDayFragment();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_day, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            this.day = (DateTime) getArguments().getSerializable(EXTRA_DATE_TIME);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        setDay(day);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
    }

    private void init() {
        categories = PreferenceHelper.getInstance().getActiveCategories(Measurement.Category.BLOODSUGAR);
        imageAdapter = new CategoryImageListAdapter(getContext());
        valueAdapter = new CategoryValueListAdapter(getContext());
    }

    private void initLayout() {
        imageTable.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        imageTable.addItemDecoration(new LinearDividerItemDecoration(getContext()));
        imageTable.setAdapter(imageAdapter);
        imageTable.setNestedScrollingEnabled(false);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), DateTimeConstants.HOURS_PER_DAY / 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        valueTable.setLayoutManager(layoutManager);
        valueTable.addItemDecoration(new TimelineGridDividerItemDecoration(getContext()));
        valueTable.setAdapter(valueAdapter);
        valueTable.setNestedScrollingEnabled(false);

        scrollView.setOnScrollChangeListener(onScrollListener);
    }

    private void invalidate() {
        if (dayChart != null) {
            dayChart.setDay(day);
        }
        if (valueTable != null) {
            DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<ListItemCategoryValue>>() {
                @Override
                public List<ListItemCategoryValue> onShouldLoad() {
                    List<ListItemCategoryValue> listItems = new ArrayList<>();
                    LinkedHashMap<Measurement.Category, ListItemCategoryValue[]> values = EntryDao.getInstance().getAverageDataTable(day, categories, SKIP_EVERY_X_HOUR);
                    for (Map.Entry<Measurement.Category, ListItemCategoryValue[]> mapEntry : values.entrySet()) {
                        Collections.addAll(listItems, mapEntry.getValue());
                    }
                    return listItems;
                }
                @Override
                public void onDidLoad(List<ListItemCategoryValue> values) {
                    temp = values;
                    if (isVisible) {
                        // Update only onPageChanged to improve performance
                        update();
                    } else if (valueAdapter.getItemCount() == 0) {
                        // Delay updating invisible fragments onStart to improve performance
                        new Handler().postDelayed(() -> update(), 500);
                    }
                }
            });
        }
    }

    public void update() {
        if (isAdded() && temp != null) {
            if (valueAdapter.getItemCount() > 0) {
                for (int index = 0; index < temp.size(); index++) {
                    ListItemCategoryValue listItem = temp.get(index);
                    RecyclerView.ViewHolder viewHolder = valueTable.findViewHolderForAdapterPosition(index);
                    if (viewHolder instanceof CategoryValueViewHolder) {
                        valueAdapter.setItem(listItem, index);
                        // We access the ViewHolder directly for better performance compared to notifyItem(Range)Changed
                        CategoryValueViewHolder categoryValueViewHolder = (CategoryValueViewHolder) viewHolder;
                        categoryValueViewHolder.setListItem(listItem);
                        ((CategoryValueViewHolder) viewHolder).bindData();
                    }
                }
            } else {
                for (Measurement.Category category : categories) {
                    imageAdapter.addItem(new ListItemCategoryImage(category));
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
            invalidate();
        }
    }

    public void setOnScrollListener(NestedScrollView.OnScrollChangeListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void scrollTo(int yOffset) {
        if (isAdded()) {
            scrollView.scrollBy(0, yOffset - valueTable.computeVerticalScrollOffset());
        }
    }
}
