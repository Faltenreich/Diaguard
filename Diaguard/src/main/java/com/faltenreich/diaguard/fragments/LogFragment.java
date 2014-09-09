package com.faltenreich.diaguard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;

public class LogFragment extends Fragment {

    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // To multiple instantiation of the same fragment
        // Needed for fragments in fragments
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_log, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        setHasOptionsMenu(true);
        return view;
    }
}