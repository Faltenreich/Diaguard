package com.faltenreich.diaguard.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.helpers.ViewHelper;
import com.faltenreich.diaguard.ui.chart.ChartViewPager;
import com.faltenreich.diaguard.ui.chart.DayChart;
import com.faltenreich.diaguard.ui.recycler.DayOfMonthDrawable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartDayFragment extends Fragment {

    public static final String EXTRA_DATE_TIME = "EXTRA_DATE_TIME";

    private DayChart dayChart;

    private DateTime dateTime;

    public static ChartDayFragment createInstance(DateTime dateTime) {
        ChartDayFragment fragment = new ChartDayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE_TIME, dateTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart_day, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getComponents(view);

        if (getArguments() != null) {
            dateTime = (DateTime) getArguments().getSerializable(EXTRA_DATE_TIME);
        }

        dayChart.setDateTime(dateTime);
    }

    private void getComponents(@NonNull View view) {
        dayChart = (DayChart) view.findViewById(R.id.day_chart);
    }

    public void setDateTime(DateTime dateTime) {
        dayChart.setDateTime(dateTime);
    }
}
