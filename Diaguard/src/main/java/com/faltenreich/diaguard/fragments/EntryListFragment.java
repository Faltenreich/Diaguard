package com.faltenreich.diaguard.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapters.ListEntry;
import com.faltenreich.diaguard.adapters.ListItem;
import com.faltenreich.diaguard.adapters.ListSection;
import com.faltenreich.diaguard.adapters.LogBaseAdapter;
import com.faltenreich.diaguard.adapters.LogEndlessAdapter;
import com.faltenreich.diaguard.adapters.PinnedSectionListView;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.ViewHelper;

import java.util.List;

public class EntryListFragment extends ListFragment {

    protected LogEndlessAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Restore the previously serialized activated item position
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeListView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initializeListView();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        ListItem listItem = (ListItem)getListView().getAdapter().getItem(position);
        if(!listItem.isSection()) {
            ListEntry listEntry = (ListEntry) listItem;
            callbackList.onItemSelected(listEntry.getEntry().getId());
        }
    }

    private void initializeListView() {
        adapter = new LogEndlessAdapter(getActivity());
        getListView().setAdapter(adapter);
        getListView().setEmptyView(getView().findViewById(R.id.listViewEventsEmpty));
        ((PinnedSectionListView)getListView()).setShadowVisible(false);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id) {
                ListItem listItem = (ListItem)getListView().getAdapter().getItem(position);
                if(!listItem.isSection()) {
                    final Entry entry = ((ListEntry) listItem).getEntry();
                    String[] actions = new String[]{
                            getString(R.string.entry_edit),
                            getString(R.string.entry_delete) };
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder
                            .setTitle(Helper.getDateFormat().print(entry.getDate()) + " " +
                                    Helper.getTimeFormat().print(entry.getDate()))
                            .setItems(actions, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            editEntry(entry);
                                            break;
                                        case 1:
                                            deleteEntry(entry);
                                            break;
                                    }
                                }
                            });
                    dialogBuilder.create().show();
                }
                return true;
            }
        });
    }

    public void editEntry(Entry entry) {
        Intent intent = new Intent(getActivity(), NewEventActivity.class);
        intent.putExtra(NewEventActivity.EXTRA_ENTRY, entry.getId());
        startActivity(intent);
    }

    public void deleteEntry(Entry entry) {
        LogBaseAdapter logBaseAdapter = ((LogBaseAdapter)((LogEndlessAdapter) getListView().getAdapter()).getAdapter());
        int positionInAdapter = logBaseAdapter.getItemPosition(entry);
        // Remove from database
        //dataSource.delete(entry);
        // Remove from ListView
        if(positionInAdapter >= 0) {
            logBaseAdapter.items.remove(positionInAdapter);
            // Remove section if entry was the last item of this section
            if(logBaseAdapter.items.get(positionInAdapter - 1) instanceof ListSection) {
                if(logBaseAdapter.items.size() <= positionInAdapter ||
                        !(logBaseAdapter.items.get(positionInAdapter) instanceof ListEntry)) {
                    logBaseAdapter.items.remove(positionInAdapter - 1);
                }
            }
            logBaseAdapter.notifyDataSetChanged();
        }
        // Close detail view
        if(ViewHelper.isLargeScreen(getActivity())) {
            List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
            for (Fragment childFragment : fragments) {
                if (childFragment instanceof EntryDetailFragment) {
                    EntryDetailFragment entryDetailFragment = (EntryDetailFragment) childFragment;
                    if(entryDetailFragment.entry.getId() == entry.getId()) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(entryDetailFragment).commit();
                    }
                }
            }
        }
        // TODO ViewHelper.showSnackbar(getActivity(), getString(R.string.entry_deleted));
    }

    private void updateListView() {
        for(int position = 0; position < getListAdapter().getCount(); position++) {
            // TODO: This is only the Wrapped adapter!
            ListItem listItem = (ListItem) getListAdapter().getItem(position);
            if(listItem instanceof ListEntry) {
                Entry entry = ((ListEntry) listItem).getEntry();
            }
        }
    }

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

    // endregion
}