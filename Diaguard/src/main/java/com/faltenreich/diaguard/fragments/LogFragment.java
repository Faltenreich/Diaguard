package com.faltenreich.diaguard.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.EntryDetailActivity;
import com.faltenreich.diaguard.R;

public class LogFragment extends Fragment implements EntryListFragment.CallbackList {

    public static final int REQUEST_EVENT_CREATED = 1;
    private boolean isLargeScreen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        setHasOptionsMenu(true);

        return view;
    }

    /**
     * Callback from MessageListFragment to respond to a selected ListItem
     */
    @Override
    public void onItemSelected(long id) {
        if (isLargeScreen) {
            // Tablet
            Bundle arguments = new Bundle();
            arguments.putLong(EntryDetailFragment.ENTRY_ID, id);
            EntryDetailFragment fragment = new EntryDetailFragment();
            fragment.setArguments(arguments);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.entry_detail, fragment)
                    .commit();
        }
        else {
            // Phone
            Intent intent = new Intent(getActivity(), EntryDetailActivity.class);
            intent.putExtra(EntryDetailFragment.ENTRY_ID, id);
            startActivity(intent);
        }
    }
}