package com.faltenreich.diaguard.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.view.DayChart;

import org.joda.time.DateTime;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDayFragment extends Fragment {

    public static final String EXTRA_DATE_TIME = "EXTRA_DATE_TIME";

    private DayChart dayChart;

    public static ChartDayFragment createInstance(DateTime dateTime) {
        ChartDayFragment fragment = new ChartDayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE_TIME, dateTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_day, container, false);
        getComponents(view);
        if (getArguments() != null) {
            DateTime dateTime = (DateTime) getArguments().getSerializable(EXTRA_DATE_TIME);
            dayChart.setDay(dateTime);
        }
        return view;
    }

    private void getComponents(@NonNull View view) {
        dayChart = (DayChart) view.findViewById(R.id.day_chart);
    }

    public DateTime getDay() {
        return dayChart != null ? dayChart.getDay() : null;
    }

    public void setDay(DateTime day) {
        dayChart.setDay(day);
    }
}
