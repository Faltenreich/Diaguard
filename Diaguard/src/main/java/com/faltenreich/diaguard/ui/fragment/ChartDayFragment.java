package com.faltenreich.diaguard.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.view.chart.CategoryChart;
import com.faltenreich.diaguard.ui.view.chart.DayChart;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDayFragment extends Fragment {

    public static final String EXTRA_DATE_TIME = "EXTRA_DATE_TIME";

    @Bind(R.id.day_chart)
    protected DayChart dayChart;

    @Bind(R.id.category_chart)
    protected CategoryChart categoryChart;

    private DateTime day;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_day, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            DateTime dateTime = (DateTime) getArguments().getSerializable(EXTRA_DATE_TIME);
            setDay(dateTime);
        }
        return view;
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        if (dayChart != null) {
            dayChart.setDay(day);
        }
        if (categoryChart != null) {
            categoryChart.setDay(day);
        }
    }
}
