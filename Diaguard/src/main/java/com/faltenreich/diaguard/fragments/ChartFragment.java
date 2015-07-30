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
import android.widget.Button;
import android.widget.DatePicker;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.ChartHelper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.ViewHelper;
import com.faltenreich.diaguard.ui.chart.ChartMarkerView;
import com.faltenreich.diaguard.ui.chart.DayChart;
import com.faltenreich.diaguard.ui.recycler.DayOfMonthDrawable;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends BaseFragment implements View.OnClickListener, OnChartValueSelectedListener {

    private DayChart chart;
    private Button buttonDate;
    private View buttonPrevious;
    private View buttonNext;

    private DateTime currentDay;

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getComponents(view);
        initialize();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateLabels();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.date, menu);

        MenuItem menuItem = menu.findItem(R.id.action_today);
        if (menuItem != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
                setTodayIcon(icon, getActivity());
            } else {
                menuItem.setIcon(R.drawable.ic_action_today);
            }
        }
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.timeline);
    }

    @Override
    public boolean hasAction() {
        return true;
    }

    @Override
    public void action(View view) {
        showDatePicker();
    }

    private void setTodayIcon(LayerDrawable icon, Context context) {
        DayOfMonthDrawable today = new DayOfMonthDrawable(context);
        today.setDayOfMonth(DateTime.now().dayOfMonth().get());
        icon.mutate();
        icon.setDrawableByLayerId(R.id.today_icon_day, today);
    }

    private void initialize() {
        currentDay = DateTime.now().withHourOfDay(0).withMinuteOfHour(0);
        initializeGUI();
    }

    private void getComponents(@NonNull View view) {
        chart = (DayChart) view.findViewById(R.id.chart);
        buttonDate = (Button) view.findViewById(R.id.button_date);
        buttonPrevious = view.findViewById(R.id.button_previous);
        buttonNext = view.findViewById(R.id.button_next);
    }

    private void initializeGUI() {
        buttonDate.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        ChartHelper.setChartDefaultStyle(chart);
        chart.setOnChartValueSelectedListener(this);
    }

    private void previousDay() {
        currentDay = currentDay.minusDays(1);
        updateLabels();
    }

    private void nextDay() {
        currentDay = currentDay.plusDays(1);
        updateLabels();
    }

    private void showDatePicker() {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                currentDay = currentDay.withYear(year).withMonthOfYear(month+1).withDayOfMonth(day);
                updateLabels();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, currentDay);
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }

    private void update() {
        updateChart();
        updateLabels();
    }

    private void updateLabels() {
        if(isAdded()) {
            String weekDay = ViewHelper.isLandscape(getActivity()) || ViewHelper.isLargeScreen(getActivity()) ?
                    currentDay.dayOfWeek().getAsText() :
                    currentDay.dayOfWeek().getAsShortText();
            String fullDate = DateTimeFormat.mediumDate().print(currentDay);
            getActionView().setText(String.format("%s, %s", weekDay, fullDate));
        }
    }

    private void updateChart() {
        chart.setData(currentDay);
    }

    //region Listeners

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_date:
                showDatePicker();
                break;
            case R.id.button_previous:
                previousDay();
                break;
            case R.id.button_next:
                nextDay();
                break;
            default:
                break;
        }
    }

    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry e, int dataSetIndex, Highlight h) {
        MarkerView markerView = new ChartMarkerView(getActivity());
        chart.setMarkerView(markerView);
    }

    @Override
    public void onNothingSelected() {
        // TODO: Dismiss MarkerView
    }

    //endregion
}
