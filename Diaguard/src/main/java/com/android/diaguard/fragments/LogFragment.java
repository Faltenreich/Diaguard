package com.android.diaguard.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.android.diaguard.MainActivity;
import com.android.diaguard.NewEventActivity;
import com.android.diaguard.R;
import com.android.diaguard.adapters.ListViewAdapterLog;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.PreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class LogFragment extends Fragment {

    private final int ACTION_EDIT = 0;
    private final int ACTION_DELETE = 1;

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    Calendar time;
    boolean[] checkedCategories;

    ListView listViewEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
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
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.log));
        updateListView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateListView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.report, menu);
    }

    public void initialize() {

        dataSource = new DatabaseDataSource(getActivity());
        preferenceHelper = new PreferenceHelper(getActivity());

        time = Calendar.getInstance();

        Event.Category[] categories = Event.Category.values();
        checkedCategories = new boolean[categories.length];
        for(int item = 0; item < categories.length; item++) {
            checkedCategories[item] = preferenceHelper.isCategoryActive(categories[item]);
        }

        getComponents();
        initializeGUI();
        updateListView();
    }

    public void getComponents() {
        listViewEvents = (ListView) getView().findViewById(R.id.listViewEvents);
    }

    public void initializeGUI() {

        getView().findViewById(R.id.button_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        getView().findViewById(R.id.button_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousDay();
            }
        });
        getView().findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDay();
            }
        });

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Event event = (Event) listViewEvents.getAdapter().getItem(position);
                editEvent(event);
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(preferenceHelper.getCategoryName(event.getCategory()))
                        .setItems(R.array.actions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case ACTION_EDIT:
                                        editEvent(event);
                                        break;
                                    case ACTION_DELETE:
                                        deleteEvent(event);
                                        updateListView();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                */
            }
        });

        /*
        listViewEvents.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                int i = 0;
            }
        });
        listViewEvents.setOnScrollListener(new AbsListView.OnScrollListener() {

            int currentFirstVisibleItem;
            int currentVisibleItemCount;
            int currentScrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
                    ((EndlessListViewAdapter)listViewEvents.getAdapter()).isScrollingDown = true;
                    ((EndlessListViewAdapter)listViewEvents.getAdapter()).isScrollingUp = true;
                }
            }
        });
        */
    }

    private void editEvent(Event event) {
        Intent intent = new Intent(getActivity(), NewEventActivity.class);
        intent.putExtra(NewEventActivity.EXTRA_ID, event.getId());
        startActivity(intent);
    }

    private void updateListView() {

        SimpleDateFormat format = preferenceHelper.getDateFormat();
        String weekDay = getResources().getStringArray(R.array.weekdays)[time.get(Calendar.DAY_OF_WEEK)-1];
        ((Button)getView().findViewById(R.id.button_date)).setText(weekDay.substring(0,2) + "., " + format.format(time.getTime()));

        dataSource.open();
        List<Event> eventsOfDay = dataSource.getEventsOfDay(time);
        dataSource.close();

        List<Event> visibleEventsOfDay = new ArrayList<Event>();
        for(Event event : eventsOfDay) {
            int category = Event.Category.valueOf(event.getCategory().name()).ordinal();
            if(checkedCategories[category])
                visibleEventsOfDay.add(event);
        }

        ListViewAdapterLog adapter = new ListViewAdapterLog(getActivity());
        adapter.events.addAll(visibleEventsOfDay);
        /*
        EndlessListViewAdapter adapter = new EndlessListViewAdapter(
                getActivity(),
                Calendar.getInstance(),
                preferenceHelper.getActiveCategories());
        */
        listViewEvents.setAdapter(adapter);

        listViewEvents.setEmptyView(getView().findViewById(R.id.listViewEventsEmpty));
    }

    // LISTENERS

    public void previousDay () {
        time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) - 1);
        updateListView();
    }

    public void nextDay () {
        time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) + 1);
        updateListView();
    }

    public void showDatePicker () {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time.set(Calendar.YEAR, year);
                time.set(Calendar.MONTH, month);
                time.set(Calendar.DAY_OF_MONTH, day);
                updateListView();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, time);
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }

    public void openFilters() {

        final boolean[] checkedCategoriesTemporary = checkedCategories.clone();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMultiChoiceItems(R.array.categories, checkedCategories,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkedCategories = checkedCategoriesTemporary.clone();
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateListView();
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void startNewEventActivity() {
        Intent intent = new Intent (getActivity(), NewEventActivity.class);
        intent.putExtra(NewEventActivity.EXTRA_DATE, time);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                openFilters();
                return true;
            case R.id.action_newevent:
                startNewEventActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}