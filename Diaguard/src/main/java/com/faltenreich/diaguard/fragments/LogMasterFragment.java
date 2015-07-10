package com.faltenreich.diaguard.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapters.LogAdapter;

import org.joda.time.DateTime;

/**
 * Created by Filip on 05.07.2015.
 */
public class LogMasterFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LogAdapter recyclerAdapter;

    private DateTime currentDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
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
        currentDateTime = DateTime.now();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new LogAdapter(getActivity());
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void appendRows(boolean isScrollingDown) {

    }

    private void fetchData(boolean isLoadingNext) {
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.log);
    }
}
