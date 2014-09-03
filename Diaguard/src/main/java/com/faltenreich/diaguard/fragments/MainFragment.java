package com.faltenreich.diaguard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class MainFragment extends Fragment {

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    DateTime time;

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
        setHasOptionsMenu(true);
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
        updateContent();
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

        dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();

        preferenceHelper = new PreferenceHelper(getActivity());

        time = new DateTime();

        int countBloodSugarMeasurements = dataSource.count(
                DatabaseHelper.MEASUREMENT,
                DatabaseHelper.CATEGORY,
                Measurement.Category.BloodSugar.toString());

        if(countBloodSugarMeasurements > 0) {
            textViewLatestValue.setTextSize(60);
            updateLatest();
            updateDashboard();
        }
        else {
            textViewLatestValue.setTextSize(40);
            textViewAverageMonth.setText(Helper.PLACEHOLDER);
            textViewAverageWeek.setText(Helper.PLACEHOLDER);
            textViewAverageDay.setText(Helper.PLACEHOLDER);
        }

        dataSource.close();
    }

    private void updateLatest() {
        Entry entry = dataSource.getLatestBloodSugar();
        Measurement latestBloodSugar = entry.getMeasurements().get(0);

        // Value
        float value = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar, latestBloodSugar.getValue());
        textViewLatestValue.setText(preferenceHelper.
                getDecimalFormat(Measurement.Category.BloodSugar).format(value));

        // Highlighting
        if(preferenceHelper.limitsAreHighlighted()) {
            if(latestBloodSugar.getValue() > preferenceHelper.getLimitHyperglycemia())
                textViewLatestValue.setTextColor(getResources().getColor(R.color.red));
            else if(latestBloodSugar.getValue() < preferenceHelper.getLimitHypoglycemia())
                textViewLatestValue.setTextColor(getResources().getColor(R.color.blue));
            else
                textViewLatestValue.setTextColor(getResources().getColor(R.color.green));
        }

        // Unit
        textViewLatestUnit.setText(preferenceHelper.getUnitAcronym(Measurement.Category.BloodSugar));

        // Time
        textViewLatestTime.setText(preferenceHelper.
                getDateFormat().print(entry.getDate()) + " " +
                Helper.getTimeFormat().print(entry.getDate()) + " | ");
        int differenceInMinutes = Minutes.minutesBetween(entry.getDate(), new DateTime()).getMinutes();

        // Highlight if last measurement is more than eight hours ago
        textViewLatestAgo.setTextColor(getResources().getColor(R.color.green));
        if(differenceInMinutes > 480)
            textViewLatestAgo.setTextColor(getResources().getColor(R.color.red));

        textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));
    }

    private void updateDashboard() {
        updateToday();
        updateAverage();
    }

    private void updateToday() {
        int measurements = dataSource.countMeasurements(time, Measurement.Category.BloodSugar);
        textViewMeasurements.setText(Integer.toString(measurements));

        int countHypers = dataSource.countMeasurements(time,
                Measurement.Category.BloodSugar,
                preferenceHelper.getLimitHyperglycemia(), true);
        textViewHyperglycemia.setText(Integer.toString(countHypers));

        int countHypos = dataSource.countMeasurements(time,
                Measurement.Category.BloodSugar,
                preferenceHelper.getLimitHypoglycemia(), false);
        textViewHypoglycemia.setText(Integer.toString(countHypos));
    }

    private void updateAverage() {
        float avgMonth = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(30));
        float avgWeek = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(7));
        float avgDay = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(1));

        String avgMonthString = preferenceHelper.
                getDecimalFormat(Measurement.Category.BloodSugar).format(avgMonth);
        if(avgMonth <= 0)
            avgMonthString = Helper.PLACEHOLDER;
        String avgWeekString = preferenceHelper.
                getDecimalFormat(Measurement.Category.BloodSugar).format(avgWeek);
        if(avgWeek <= 0)
            avgWeekString = Helper.PLACEHOLDER;
        String avgDayString = preferenceHelper.
                getDecimalFormat(Measurement.Category.BloodSugar).format(avgDay);
        if(avgDay <= 0)
            avgDayString = Helper.PLACEHOLDER;

        textViewAverageMonth.setText(avgMonthString);
        textViewAverageWeek.setText(avgWeekString);
        textViewAverageDay.setText(avgDayString);
    }
}
