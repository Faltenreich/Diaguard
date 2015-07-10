package com.faltenreich.diaguard.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapters.recycler.EndlessScrollListener;
import com.faltenreich.diaguard.adapters.recycler.LogAdapter;

import org.joda.time.DateTime;

/**
 * Created by Filip on 05.07.2015.
 */
public class LogMasterFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LogAdapter recyclerAdapter;

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

    private void getComponents(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
    }

    private void initialize() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new LogAdapter(getActivity());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(Direction direction) {
                recyclerAdapter.appendRows(direction);
            }
        });
        scrollToDay(DateTime.now());
    }

    private void scrollToDay(DateTime day) {
        recyclerView.scrollToPosition(day.dayOfMonth().getMaximumValue() - day.dayOfMonth().get() + 1);
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.log);
    }
}
