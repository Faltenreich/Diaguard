package com.faltenreich.diaguard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.BaseActivity;
import com.faltenreich.diaguard.R;

/**
 * Created by Filip on 26.06.2015.
 */
public abstract class BaseFragment extends Fragment {

    private TextView actionView;

    public abstract String getTitle();

    public abstract boolean hasAction();

    public abstract void action(View view);

    public TextView getActionView() {
        return actionView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (toolbar != null && getActivity() instanceof BaseActivity) {
            actionView = (TextView) toolbar.findViewById(R.id.action);
            ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                if (hasAction()) {
                    actionBar.setDisplayShowTitleEnabled(false);
                    actionView.setVisibility(View.VISIBLE);
                    actionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            action(actionView);
                        }
                    });
                } else {
                    actionBar.setDisplayShowTitleEnabled(false);
                    actionView.setVisibility(View.GONE);
                }
            }
        }
    }
}
