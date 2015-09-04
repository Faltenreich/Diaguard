package com.faltenreich.diaguard.ui.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.ui.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by Filip on 26.06.2015.
 */
public abstract class BaseFragment extends Fragment {

    private int layoutResourceId;
    private String title;

    private BaseFragment() {
        // Forbidden
    }

    public BaseFragment(@LayoutRes int layoutResourceId) {
        this();
        this.layoutResourceId = layoutResourceId;
    }

    public BaseFragment(@LayoutRes int layoutResourceId, @StringRes int titleResourceId) {
        this();
        this.layoutResourceId = layoutResourceId;
        this.title = getString(titleResourceId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResourceId, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof BaseActivity) {
            View actionView = getActionView();
            if (this instanceof ToolbarCallback) {
                actionView.setVisibility(View.VISIBLE);
                actionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ToolbarCallback) BaseFragment.this).action();
                    }
                });
            } else {
                actionView.setVisibility(View.GONE);
            }
        }
    }

    public TextView getActionView() {
        return ((BaseActivity) getActivity()).getActionView();
    }

    public String getTitle() {
        return title;
    }

    interface ToolbarCallback {
        void action();
    }
}
