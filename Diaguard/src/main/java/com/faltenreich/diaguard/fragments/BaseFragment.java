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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Filip on 26.06.2015.
 */
public abstract class BaseFragment extends Fragment {

    public abstract String getTitle();

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

    interface ToolbarCallback {
        void action();
    }
}
