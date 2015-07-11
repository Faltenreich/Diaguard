package com.faltenreich.diaguard.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.recycler.EndlessScrollListener;
import com.faltenreich.diaguard.ui.recycler.LogRecyclerAdapter;
import com.faltenreich.diaguard.ui.recycler.RecyclerItem;

import org.joda.time.DateTime;

/**
 * Created by Filip on 05.07.2015.
 */
public class LogMasterFragment extends BaseFragment {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private LogRecyclerAdapter recyclerAdapter;

    private DateTime firstVisibleDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_master, container, false);
        setHasOptionsMenu(true);
        getComponents(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: notifyItemInserted() instead of reloading all onResume()
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

        // FIXME
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        final View actionView = toolbar.findViewById(R.id.action);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action(actionView);
            }
        });
    }

    private void goToDay(DateTime day) {
        firstVisibleDay = day;

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new LogRecyclerAdapter(getActivity(), day);
        recyclerView.setAdapter(recyclerAdapter);
        int firstVisiblePosition = day.dayOfMonth().get();
        if (firstVisiblePosition == 1) {
            // Workaround to support showing header instead of first day
            firstVisiblePosition = day.dayOfMonth().getMaximumValue() + firstVisiblePosition - 1;
        }
        recyclerView.scrollToPosition(firstVisiblePosition);
        getActionView().setText(day.toString("MMMM"));

        // Endless scroll
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(Direction direction) {
                recyclerAdapter.appendRows(direction);
            }
        });

        // Fragment updates
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerItem item = recyclerAdapter.items.get(linearLayoutManager.findFirstVisibleItemPosition());
                firstVisibleDay = item.getDateTime();

                updateMonthForUi(dy < 0);
            }
        });
    }

    private void updateMonthForUi(boolean isScrollingUp) {
        // Update month in Toolbar when section is being crossed
        boolean isCrossingMonth = isScrollingUp ?
                firstVisibleDay.dayOfMonth().get() == firstVisibleDay.dayOfMonth().getMaximumValue() :
                firstVisibleDay.dayOfMonth().get() == firstVisibleDay.dayOfMonth().getMinimumValue();
        if (isCrossingMonth) {
            getActionView().setText(firstVisibleDay.toString("MMMM"));
        }
    }

    public void showDatePicker () {
        DatePickerFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                goToDay(DateTime.now().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day));
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, firstVisibleDay);
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
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

    @Override
    public boolean hasAction() {
        return true;
    }

    @Override
    public void action(View view) {
        showDatePicker();
    }
}
