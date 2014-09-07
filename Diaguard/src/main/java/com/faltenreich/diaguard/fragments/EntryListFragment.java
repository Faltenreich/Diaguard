package com.faltenreich.diaguard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.faltenreich.diaguard.MainActivity;
import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapters.ListViewAdapterLog;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class EntryListFragment extends ListFragment {

    // region Callbacks

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private int activatedPosition = ListView.INVALID_POSITION;

    /**
     * Callback for MainActivity to get a selected ListItem
     */
    public interface CallbackList {
        public void onItemSelected(long id);
    }

    private CallbackList callbackList = templateCallbackList;
    private static CallbackList templateCallbackList = new CallbackList() {
        @Override
        public void onItemSelected(long id) {
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof CallbackList)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        callbackList = (CallbackList) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbackList = templateCallbackList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (activatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, activatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION)
            getListView().setItemChecked(activatedPosition, false);
        else
            getListView().setItemChecked(position, true);
        activatedPosition = position;
    }

    private final int ACTION_EDIT = 0;
    private final int ACTION_DELETE = 1;

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    DateTime time;
    boolean[] checkedCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
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
        initializeGUI();
        updateListView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initializeGUI();
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
    }

    public void initializeGUI() {

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

        getView().findViewById(R.id.button_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Entry selectedEntry = (Entry)getListView().getAdapter().getItem(position);
        callbackList.onItemSelected(selectedEntry.getId());
    }

    private void updateListView() {

        DateTimeFormatter format = preferenceHelper.getDateFormat();
        String weekDay = getResources().getStringArray(R.array.weekdays)[time.getDayOfWeek()-1];
        ((Button)getView().findViewById(R.id.button_date)).setText(weekDay.substring(0,2) + "., " + format.print(time));

        dataSource.open();
        List<Entry> entriesOfDay = dataSource.getEntriesOfDay(time);
        for(Entry entry : entriesOfDay) {
            // TODO: Limit depending on space and order by category
            List<Model> measurements = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                    DatabaseHelper.ENTRY_ID + "=?", new String[]{Long.toString(entry.getId())},
                    null, null, null, null);
            for(Model model : measurements)
                entry.getMeasurements().add((Measurement)model);
        }
        dataSource.close();

        /*
        // TODO
        List<Event> visibleEventsOfDay = new ArrayList<Event>();
        for(Event event : entriesOfDay) {
            int category = Event.Category.valueOf(event.getCategory().name()).ordinal();
            if(checkedCategories[category])
                visibleEventsOfDay.add(event);
        }
        */

        ListViewAdapterLog adapter = new ListViewAdapterLog(getActivity());
        adapter.entries.addAll(entriesOfDay);
        getListView().setAdapter(adapter);

        getListView().setEmptyView(getView().findViewById(R.id.listViewEventsEmpty));
    }

    // LISTENERS

    public void previousDay() {
        time = time.minusDays(1);
        updateListView();
    }

    public void nextDay() {
        time = time.plusDays(1);
        updateListView();
    }

    public void showDatePicker () {
        DatePickerFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month+1).withDayOfMonth(day);
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