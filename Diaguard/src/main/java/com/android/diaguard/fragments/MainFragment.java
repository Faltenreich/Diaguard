package com.android.diaguard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.diaguard.MainActivity;
import com.android.diaguard.NewEventActivity;
import com.android.diaguard.R;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainFragment extends Fragment {

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    DecimalFormat format;
    Calendar time;

    TextView textViewLatestValue;
    TextView textViewLatestUnit;
    TextView textViewLatestTime;
    TextView textViewLatestAgo;

    TextView textViewMeasurements;
    TextView textViewHyperglycemia;
    TextView textViewHypoglycemia;

    TextView textViewAverageMonth;
    TextView textViewAverageWeek;
    TextView textViewAverageDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponents();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        updateContent();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    private void getComponents() {
        textViewLatestValue = (TextView) getView().findViewById(R.id.textview_latest_value);
        textViewLatestUnit = (TextView) getView().findViewById(R.id.textview_latest_unit);
        textViewLatestTime = (TextView) getView().findViewById(R.id.textview_latest_time);
        textViewLatestAgo = (TextView) getView().findViewById(R.id.textview_latest_ago);

        textViewMeasurements = (TextView) getView().findViewById(R.id.textview_measurements);
        textViewHyperglycemia = (TextView) getView().findViewById(R.id.textview_hyperglycemia);
        textViewHypoglycemia = (TextView) getView().findViewById(R.id.textview_hypoglycemia);

        textViewAverageMonth = (TextView) getView().findViewById(R.id.textview_avg_month);
        textViewAverageWeek = (TextView) getView().findViewById(R.id.textview_avg_week);
        textViewAverageDay = (TextView) getView().findViewById(R.id.textview_avg_day);
    }

    private void updateContent() {

        getView().findViewById(R.id.button_newevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewEventActivity.class));
            }
        });

        dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();

        preferenceHelper = new PreferenceHelper(getActivity());

        time = Calendar.getInstance();

        if(dataSource.countEvents(Event.Category.BloodSugar) > 0) {
            format = Helper.getDecimalFormat();
            updateLatest();
            updateDashboard();
        }
        else {
            textViewAverageMonth.setText(Helper.PLACEHOLDER);
            textViewAverageWeek.setText(Helper.PLACEHOLDER);
            textViewAverageDay.setText(Helper.PLACEHOLDER);
        }

        dataSource.close();
    }

    private void updateLatest() {

        Event latestEvent = dataSource.getLatestEvent(Event.Category.BloodSugar);

        // Value
        float value = preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, latestEvent.getValue());
        textViewLatestValue.setText(format.format(value));

        textViewLatestUnit.setText(preferenceHelper.getUnitAcronym(Event.Category.BloodSugar));

        // Time
        textViewLatestTime.setText(preferenceHelper.getDateAndTimeFormat().format(latestEvent.getDate().getTime()) + " | ");
        int differenceInMinutes = Helper.getDifferenceInMinutes(latestEvent.getDate(), Calendar.getInstance());

        // Highlight if last measurement is more than eight hours ago
        textViewLatestAgo.setTextColor(getResources().getColor(R.color.green_lt));
        if(differenceInMinutes > 480)
            textViewLatestAgo.setTextColor(getResources().getColor(R.color.red));

        textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));
    }

    private void updateDashboard() {
        updateToday();
        updateAverage();
    }

    private void updateToday() {
        int measurements = dataSource.countEventsOfDay(time, Event.Category.BloodSugar);
        textViewMeasurements.setText(Integer.toString(measurements) + "x");

        int countHypers = dataSource.countEventsAboveValue(time,
                Event.Category.BloodSugar,
                preferenceHelper.getLimitHyperglycemia());
        textViewHyperglycemia.setText(Integer.toString(countHypers) + "x");

        int countHypos = dataSource.countEventsBelowValue(time,
                Event.Category.BloodSugar,
                preferenceHelper.getLimitHypoglycemia());
        textViewHypoglycemia.setText(Integer.toString(countHypos) + "x");
    }

    private void updateAverage() {
        float avgMonth = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(30));
        float avgWeek = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(7));
        float avgDay = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(1));

        if(avgMonth > 20)
            format = new DecimalFormat("#");

        String avgMonthString = format.format(avgMonth);
        if(avgMonth <= 0)
            avgMonthString = Helper.PLACEHOLDER;
        String avgWeekString = format.format(avgWeek);
        if(avgWeek <= 0)
            avgWeekString = Helper.PLACEHOLDER;
        String avgDayString = format.format(avgDay);
        if(avgDay <= 0)
            avgDayString = Helper.PLACEHOLDER;

        textViewAverageMonth.setText(avgMonthString);
        textViewAverageWeek.setText(avgWeekString);
        textViewAverageDay.setText(avgDayString);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_newevent:
                startActivity(new Intent (getActivity(), NewEventActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
