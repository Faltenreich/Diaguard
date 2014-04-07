package com.android.diaguard.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.android.diaguard.NewEventActivity;
import com.android.diaguard.R;
import com.android.diaguard.adapters.ListViewAdapterEvents;
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

        GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float sensitivity = 250;
                // Horizontal Swipe
                // Left
                if((e2.getX() - e1.getX()) > sensitivity){
                    time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) - 1);
                    updateListView();
                }
                // Right
                else if((e1.getX() - e2.getX()) > sensitivity){
                    time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) + 1);
                    updateListView();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        final GestureDetector gestureDetector = new GestureDetector(getActivity(),simpleOnGestureListener);
        listViewEvents.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Event event = (Event) listViewEvents.getAdapter().getItem(position);
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
            }
        });
    }

    private void editEvent(Event event) {
        Intent intent = new Intent(getActivity(), NewEventActivity.class);
        intent.putExtra("ID", event.getId());
        startActivity(intent);
    }

    private void deleteEvent(Event event) {
        dataSource.open();
        dataSource.deleteEvent(event);
        dataSource.close();
    }

    private void updateListView() {

        SimpleDateFormat format = preferenceHelper.getDateFormat();
        String weekDay = getResources().getStringArray(R.array.weekdays)[time.get(Calendar.DAY_OF_WEEK)-1];
        // TODO: set date

        dataSource.open();
        List<Event> eventsOfDay = dataSource.getEventsOfDay(time);
        dataSource.close();

        List<Event> visibleEventsOfDay = new ArrayList<Event>();
        for(Event event : eventsOfDay) {
            int category = Event.Category.valueOf(event.getCategory().name()).ordinal();
            if(checkedCategories[category])
                visibleEventsOfDay.add(event);
        }

        ListViewAdapterEvents adapter = new ListViewAdapterEvents(getActivity(), visibleEventsOfDay);
        listViewEvents.setAdapter(adapter);
    }

    // LISTENERS

    public void onClickPrevious (View view) {
        if(view.getTag() != null) {
            int days = Integer.parseInt(view.getTag().toString());
            if(days > 7)
                time.set(Calendar.MONTH, time.get(Calendar.MONTH) - 1);
            else
                time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) - days);
        }
        else
            time.set(Calendar.MONTH, time.get(Calendar.MONTH) - 1);
        updateListView();
    }

    public void onClickNext (View view) {
        if(view.getTag() != null) {
            int days = Integer.parseInt(view.getTag().toString());
            if(days > 7)
                time.set(Calendar.MONTH, time.get(Calendar.MONTH) + 1);
            else
                time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) + days);
        }
        else
            time.set(Calendar.MONTH, time.get(Calendar.MONTH) + 1);
        updateListView();
    }

    public void onClickShowDatePicker (View view) {
        DialogFragment newFragment = new DatePickerFragment(time) {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time.set(Calendar.YEAR, year);
                time.set(Calendar.MONTH, month);
                time.set(Calendar.DAY_OF_MONTH, day);
                updateListView();
            }
        };
        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }

    public void startNewEventActivity() {
        Intent intent = new Intent (getActivity(), NewEventActivity.class);
        intent.putExtra("Date", time);
        startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                openFilters();
                return true;
            case R.id.action_newevent:
                startActivity(new Intent(getActivity(), NewEventActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}