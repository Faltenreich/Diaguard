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
import android.widget.ListView;

import com.android.diaguard.MainActivity;
import com.android.diaguard.NewEventActivity;
import com.android.diaguard.R;
import com.android.diaguard.adapters.ListItem;
import com.android.diaguard.adapters.ListViewAdapter;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    public void initialize() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.statistics));

        preferenceHelper = new PreferenceHelper(getActivity());

        getComponents();

        dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();
        updateContent();
        dataSource.close();
    }

    private void getComponents() {
        listView = (ListView)getActivity().findViewById(R.id.listview_statistics);
    }

    private void updateContent() {
        List<ListItem> statistics = new ArrayList<ListItem>();

        statistics.add(new ListItem(getString(R.string.recently), null, true));
        String avgString = Helper.PLACEHOLDER;

        float avgDay = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(1));

        if(avgDay > 0)
            avgString = preferenceHelper.getDecimalFormat(Event.Category.BloodSugar).format(avgDay);
        statistics.add(new ListItem(getString(R.string.statistics_day), avgString));

        float avgWeek = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(7));

        avgString = Helper.PLACEHOLDER;
        if(avgWeek > 0)
            avgString = preferenceHelper.getDecimalFormat(Event.Category.BloodSugar).format(avgWeek);
        statistics.add(new ListItem(getString(R.string.statistics_week), avgString));

        float avgWeeks = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(14));

        avgString = Helper.PLACEHOLDER;
        if(avgWeeks > 0)
            avgString = preferenceHelper.getDecimalFormat(Event.Category.BloodSugar).format(avgWeeks);
        statistics.add(new ListItem(getString(R.string.statistics_weeks), avgString));

        float avgMonth = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(30));

        avgString = Helper.PLACEHOLDER;
        if(avgMonth > 0)
            avgString = preferenceHelper.getDecimalFormat(Event.Category.BloodSugar).format(avgMonth);
        statistics.add(new ListItem(getString(R.string.statistics_month), avgString));

        ListViewAdapter adapter =
                new ListViewAdapter(getActivity(), statistics);
        listView.setAdapter(adapter);
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
