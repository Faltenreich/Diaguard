package com.faltenreich.diaguard.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.faltenreich.diaguard.MainActivity;
import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class LogFragment extends Fragment {

    private final int ACTION_EDIT = 0;
    private final int ACTION_DELETE = 1;

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    DateTime time;
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
        updateListView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateListView();
    }

    public void initialize() {

        dataSource = new DatabaseDataSource(getActivity());
        preferenceHelper = new PreferenceHelper(getActivity());

        time = new DateTime();

        Measurement.Category[] categories = Measurement.Category.values();
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
                // TODO
                //final Measurement measurement = (Measurement) listViewEvents.getAdapter().getItem(position);
                //editEntry(measurement);
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(preferenceHelper.getCategoryName(event.getCategory()))
                        .setItems(R.array.actions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case ACTION_EDIT:
                                        editEntry(event);
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

    private void editEntry(Entry entry) {
        Intent intent = new Intent(getActivity(), NewEventActivity.class);
        intent.putExtra(NewEventActivity.EXTRA_ENTRY, entry.getId());
        startActivity(intent);
    }

    private void updateListView() {

        DateTimeFormatter format = preferenceHelper.getDateFormat();
        String weekDay = getResources().getStringArray(R.array.weekdays)[time.getDayOfWeek()];
        ((Button)getView().findViewById(R.id.button_date)).setText(weekDay.substring(0,2) + "., " + format.print(time));

        // TODO
        /*
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
        listViewEvents.setAdapter(adapter);

        listViewEvents.setEmptyView(getView().findViewById(R.id.listViewEventsEmpty));
        */
    }

    // LISTENERS

    public void previousDay() {
        time = time.withDayOfMonth(time.getDayOfMonth() - 1);
        updateListView();
    }

    public void nextDay() {
        time = time.withDayOfMonth(time.getDayOfMonth() + 1);
        updateListView();
    }

    public void showDatePicker () {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month).withDayOfMonth(day);
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
        getActivity().startActivityForResult(intent, MainActivity.REQUEST_EVENT_CREATED);
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