package com.faltenreich.diaguard.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.BaseActivity;
import com.faltenreich.diaguard.R;

import butterknife.ButterKnife;

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

    protected abstract int getContentViewId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actionView = (TextView) getToolbar().findViewById(R.id.action);
        ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && actionView != null) {
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

    public Toolbar getToolbar() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getToolbar();
        } else {
            throw new Resources.NotFoundException("Resource not found: R.id.toolbar");
        }
    }
}
