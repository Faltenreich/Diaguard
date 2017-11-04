package com.faltenreich.diaguard.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.view.chart.CategoryTable;
import com.faltenreich.diaguard.ui.view.chart.DayChart;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDayFragment extends Fragment {

    public static final String EXTRA_DATE_TIME = "EXTRA_DATE_TIME";

    @BindView(R.id.day_chart) DayChart dayChart;
    @BindView(R.id.category_table) CategoryTable categoryTable;

    private DateTime day;
    private RecyclerView.OnScrollListener onScrollListener;

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

    private void init() {
        categoryTable.addOnScrollListener(onScrollListener);
        setDay(day);
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        if (dayChart != null) {
            dayChart.setDay(day);
        }
        if (categoryTable != null) {
            categoryTable.setDay(day);
        }
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void scrollTo(int yOffset) {
        categoryTable.scrollTo(yOffset);
    }
}
