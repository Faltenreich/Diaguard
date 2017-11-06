package com.faltenreich.diaguard.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.CategoryRecyclerAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValues;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.chart.DayChart;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.TimelineTableTask;

import org.joda.time.DateTime;

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
    private List<ListItemCategoryValues> tableValues;
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
        init();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
    }

    private void init() {
        Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
        categories = Arrays.copyOfRange(activeCategories, 1, activeCategories.length);
        categoryTable.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        categoryTable.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryTable.setHasFixedSize(true);
        adapter = new CategoryRecyclerAdapter(getContext());
        categoryTable.setAdapter(adapter);
        categoryTable.addOnScrollListener(onScrollListener);
        setDay(day);
    }

    private void invalidate() {
        if (dayChart != null) {
            dayChart.setDay(day);
        }
        if (categoryTable != null) {
            new TimelineTableTask(getContext(), day, categories, new BaseAsyncTask.OnAsyncProgressListener<List<ListItemCategoryValues>>() {
                @Override
                public void onPostExecute(List<ListItemCategoryValues> values) {
                    tableValues = values;
                    if (isVisible) {
                        update();
                    }
                }
            }).execute();
        }
    }

    public void update() {
        if (tableValues != null) {
            // Other notify methods lead to rendering issues on view paging
            adapter.clear();
            adapter.addItems(tableValues);
            adapter.notifyDataSetChanged();
        }
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        invalidate();
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void scrollTo(int yOffset) {
        categoryTable.scrollBy(0, yOffset - categoryTable.computeVerticalScrollOffset());
    }
}
