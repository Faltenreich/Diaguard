package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.CategoryRecyclerAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemCategory;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValue;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.chart.DayChart;
import com.faltenreich.diaguard.ui.view.viewholder.CategoryValueViewHolder;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.TimelineTableTask;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDayFragment extends Fragment {

    public static final String EXTRA_DATE_TIME = "EXTRA_DATE_TIME";

    @BindView(R.id.day_chart) DayChart dayChart;
    @BindView(R.id.category_table) RecyclerView categoryTable;

    private DateTime day;
    private RecyclerView.OnScrollListener onScrollListener;
    private CategoryRecyclerAdapter adapter;
    private Measurement.Category[] categories;
    private List<ListItemCategory> tableValues;
    private boolean isVisible;

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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
    }

    private void init() {
        Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
        categories = Arrays.copyOfRange(activeCategories, 1, activeCategories.length);
        adapter = new CategoryRecyclerAdapter(getContext());
    }

    private void initLayout() {
        categoryTable.setLayoutManager(new GridLayoutManager(getContext(), (DateTimeConstants.HOURS_PER_DAY / 2) + 1));
        categoryTable.setAdapter(adapter);
        categoryTable.addOnScrollListener(onScrollListener);
        setDay(day);
    }

    private void invalidate() {
        if (dayChart != null) {
            dayChart.setDay(day);
        }
        if (categoryTable != null) {
            new TimelineTableTask(getContext(), day, categories, new BaseAsyncTask.OnAsyncProgressListener<List<ListItemCategory>>() {
                @Override
                public void onPostExecute(List<ListItemCategory> values) {
                    tableValues = values;
                    if (isVisible) {
                        // Update only onPageChanged to improve performance
                        update();
                    } else if (adapter.getItemCount() == 0) {
                        // Delay updating invisible fragments onStart to improve performance
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                update();
                            }
                        }, 500);
                    }
                }
            }).execute();
        }
    }

    public void update() {
        if (isAdded() && tableValues != null) {
            if (adapter.getItemCount() > 0) {
                for (int index = 0; index < tableValues.size(); index++) {
                    ListItemCategory listItem = tableValues.get(index);
                    RecyclerView.ViewHolder viewHolder = categoryTable.findViewHolderForAdapterPosition(index);
                    if (listItem instanceof ListItemCategoryValue && viewHolder != null && viewHolder instanceof CategoryValueViewHolder) {
                        adapter.setItem(listItem, index);
                        // We access the ViewHolder directly for better performance compared to notifyItem(Range)Changed
                        CategoryValueViewHolder categoryValueViewHolder = (CategoryValueViewHolder) viewHolder;
                        categoryValueViewHolder.setListItem((ListItemCategoryValue) listItem);
                        ((CategoryValueViewHolder) viewHolder).bindData();
                    }
                }
            } else {
                // Other notify methods lead to rendering issues on view paging
                adapter.addItems(tableValues);
                adapter.notifyDataSetChanged();
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

    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void scrollTo(int yOffset) {
        if (isAdded()) {
            categoryTable.scrollBy(0, yOffset - categoryTable.computeVerticalScrollOffset());
        }
    }
}
