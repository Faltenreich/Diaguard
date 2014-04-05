package com.android.diaguard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    TextView textViewLatestValue;
    TextView textViewLatestAgo;
    TextView textViewAverageMonth;
    TextView textViewAverageWeek;
    TextView textViewAverageDay;
    ImageView imageViewTrend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize()  {

        getComponents();

        getView().findViewById(R.id.linearlayout_newevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewEventActivity.class));
            }
        });

        getView().findViewById(R.id.linearlayout_timeline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new TimelineFragment())
                        .commit();
            }
        });

        dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();

        if(dataSource.countEvents(Event.Category.BloodSugar) > 0) {
            preferenceHelper = new PreferenceHelper(getActivity());
            format = Helper.getDecimalFormat();
            setBoxCurrent();
            setInfoBox();
        }
        else {
            textViewLatestValue.setText(Helper.PLACEHOLDER);
            textViewLatestAgo.setText(getString(R.string.notyet));

            textViewAverageMonth.setText(Helper.PLACEHOLDER);
            textViewAverageWeek.setText(Helper.PLACEHOLDER);
            textViewAverageDay.setText(Helper.PLACEHOLDER);
        }

        dataSource.close();
    }

    private void getComponents() {
        textViewLatestValue = (TextView) getView().findViewById(R.id.textview_latest_value);
        textViewLatestAgo = (TextView) getView().findViewById(R.id.textview_latest_ago);

        textViewAverageMonth = (TextView) getView().findViewById(R.id.textview_avg_month);
        textViewAverageWeek = (TextView) getView().findViewById(R.id.textview_avg_week);
        textViewAverageDay = (TextView) getView().findViewById(R.id.textview_avg_day);
        imageViewTrend = (ImageView) getView().findViewById(R.id.imageview_trend);
    }

    private void setBoxCurrent() {
        Calendar now = Calendar.getInstance();
        Event latestEvent = dataSource.getLatestEvent(Event.Category.BloodSugar);

        float value = preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, latestEvent.getValue());
        textViewLatestValue.setText(format.format(value));

        int difference = Helper.getDifferenceInMinutes(latestEvent.getDate(), now);
        String textAgo = getString(R.string.latest);
        textAgo = textAgo.replace("[value]", Integer.toString(difference));
        textAgo = textAgo.replace("[unit]", getString(R.string.minutes));
        textViewLatestAgo.setText(textAgo);
    }

    private void setInfoBox() {
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

        // Trend arrow
        float targetValue = preferenceHelper.getTargetValue();
        float monthOffset = (targetValue - avgMonth) * (-1); // how far from good?
        float weekOffset = (targetValue - avgWeek) * (-1);
        float difference = monthOffset - weekOffset; // the higher the better, negative is worse
        // which difference should be visualized?
        float sensitivity = 30 * preferenceHelper.getUnitValue(Event.Category.BloodSugar);

        // TODO: Infinitely adjustment, better calculation, testing
        if(difference > sensitivity) {
            imageViewTrend.setImageResource(R.drawable.arrow_up);
        }
        else if(difference < (-sensitivity)) {
            imageViewTrend.setImageResource(R.drawable.arrow_down);
        }
        else {
            imageViewTrend.setImageResource(R.drawable.arrow_neutral);
        }
    }
}
