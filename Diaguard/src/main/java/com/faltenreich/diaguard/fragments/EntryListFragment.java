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
import android.widget.ListView;

import com.faltenreich.diaguard.MainActivity;
import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapters.ListEntry;
import com.faltenreich.diaguard.adapters.ListItem;
import com.faltenreich.diaguard.adapters.LogEndlessAdapter;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import org.joda.time.DateTime;

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
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        ListItem listItem = (ListItem)getListView().getAdapter().getItem(position);
        if(!listItem.isSection()) {
            ListEntry listEntry = (ListEntry) getListView().getAdapter().getItem(position);
            callbackList.onItemSelected(listEntry.getEntry().getId());
        }
    }

    private void updateListView() {
        LogEndlessAdapter adapter = new LogEndlessAdapter(getActivity());
        getListView().setAdapter(adapter);
        getListView().setEmptyView(getView().findViewById(R.id.listViewEventsEmpty));
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