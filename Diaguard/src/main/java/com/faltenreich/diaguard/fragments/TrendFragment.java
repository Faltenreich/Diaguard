package com.faltenreich.diaguard.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendFragment extends BaseFragment {

    private enum Interval {
        WEEK,
        MONTH,
        YEAR
    }

    private Interval currentInterval;

    public TrendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_trend, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponents();
        initialize();
    }

    private void getComponents() {

    }

    private void initialize() {
        currentInterval = Interval.WEEK;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.interval, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void switchInterval() {
        switch (currentInterval) {
            case WEEK:
                currentInterval = Interval.MONTH;
                break;
            case MONTH:
                currentInterval = Interval.YEAR;
                break;
            case YEAR:
                currentInterval = Interval.WEEK;
                break;
        }
    }

    private int getIntervalIcon() {
        switch (currentInterval) {
            case WEEK:
                return R.drawable.ic_month;
            case MONTH:
                return R.drawable.ic_year;
            case YEAR:
                return R.drawable.ic_week;
            default:
                return 0;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_interval:
                item.setIcon(getIntervalIcon());
                switchInterval();
                break;
        }
        return true;
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.trend);
    }
}
