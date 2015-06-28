package com.faltenreich.diaguard.fragments;

import android.support.v4.app.Fragment;

import com.faltenreich.diaguard.BaseActivity;
import com.faltenreich.diaguard.database.DatabaseHelper;

/**
 * Created by Filip on 26.06.2015.
 */
public class BaseFragment extends Fragment {

    public DatabaseHelper getDatabaseHelper() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getHelper();
        } else {
            return null;
        }
    }
}
