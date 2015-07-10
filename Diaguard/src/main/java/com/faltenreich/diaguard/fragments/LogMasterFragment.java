package com.faltenreich.diaguard.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.recycler.EndlessScrollListener;
import com.faltenreich.diaguard.ui.recycler.LogRecyclerAdapter;

import org.joda.time.DateTime;

/**
 * Created by Filip on 05.07.2015.
 */
public class LogMasterFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LogRecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_master, container, false);
        setHasOptionsMenu(true);
        getComponents(view);
        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.date, menu);
    }

    private void getComponents(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
    }

    private void initialize() {
        goToDay(DateTime.now());
    }

    private void goToDay(DateTime day) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new LogRecyclerAdapter(getActivity(), day);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.scrollToPosition(day.dayOfMonth().getMaximumValue() - day.dayOfMonth().get() + 1);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(Direction direction) {
                recyclerAdapter.appendRows(direction);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_today:
                goToDay(DateTime.now());
                break;
        }
        return true;
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.log);
    }
}
