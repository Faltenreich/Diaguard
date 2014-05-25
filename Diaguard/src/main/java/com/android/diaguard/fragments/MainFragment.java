package com.android.diaguard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.diaguard.CalculatorActivity;
import com.android.diaguard.MainActivity;
import com.android.diaguard.NewEventActivity;
import com.android.diaguard.R;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.ChartHelper;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainFragment extends Fragment {

    private final String NORMAL = "Normal";
    private final String HYPERGLYCEMIA = "Hyperglycemia";
    private final String HYPOGLYCEMIA = "Hypoglycemia";

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    DecimalFormat format;
    ChartHelper chartHelper;

    TextView textViewLatestValue;
    TextView textViewLatestAgo;
    TextView textViewAverageMonth;
    TextView textViewAverageWeek;
    TextView textViewAverageDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    private void getComponents() {
        textViewLatestValue = (TextView) getView().findViewById(R.id.textview_latest_value);
        textViewLatestAgo = (TextView) getView().findViewById(R.id.textview_latest_ago);

        textViewAverageMonth = (TextView) getView().findViewById(R.id.textview_avg_month);
        textViewAverageWeek = (TextView) getView().findViewById(R.id.textview_avg_week);
        textViewAverageDay = (TextView) getView().findViewById(R.id.textview_avg_day);
    }

    private void updateContent() {

        getView().findViewById(R.id.image_newevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewEventActivity.class));
            }
        });

        getView().findViewById(R.id.image_calculator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CalculatorActivity.class));
            }
        });

        getView().findViewById(R.id.layout_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(MainActivity.FragmentType.Timeline);
            }
        });

        dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();

        preferenceHelper = new PreferenceHelper(getActivity());

        if(dataSource.countEvents(Event.Category.BloodSugar) > 0) {
            textViewLatestValue.setTextSize(34);
            format = Helper.getDecimalFormat();
            setLatestBloodSugar();
            setAverageBloodSugar();
        }
        else {
            textViewAverageMonth.setText(Helper.PLACEHOLDER);
            textViewAverageWeek.setText(Helper.PLACEHOLDER);
            textViewAverageDay.setText(Helper.PLACEHOLDER);
            textViewLatestValue.setTextSize(24);

            getView().findViewById(R.id.layout_latest).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewEventActivity.class);
                    startActivity(intent);
                }
            });
        }

        dataSource.close();
    }

    private void setLatestBloodSugar() {

        final Event latestEvent = dataSource.getLatestEvent(Event.Category.BloodSugar);

        float value = preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, latestEvent.getValue());
        textViewLatestValue.setText(format.format(value));

        int differenceInMinutes = Helper.getDifferenceInMinutes(latestEvent.getDate(), Calendar.getInstance());

        // Highlight if last measurement is more than eight hours ago
        textViewLatestAgo.setTextColor(getResources().getColor(android.R.color.darker_gray));
        if(differenceInMinutes > 480)
            textViewLatestAgo.setTextColor(getResources().getColor(R.color.red));

        textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));

        getView().findViewById(R.id.layout_latest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                intent.putExtra("ID", latestEvent.getId());
                startActivity(intent);
            }
        });
    }

    private void setAverageBloodSugar() {
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
}
