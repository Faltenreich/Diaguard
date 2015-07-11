package com.faltenreich.diaguard.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends BaseFragment {

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.overview);
    }

    @Override
    public boolean hasAction() {
        return false;
    }

    @Override
    public void action(View view) {}
}
