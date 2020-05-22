package com.faltenreich.diaguard.feature.preference.about;

import android.os.Bundle;

import androidx.preference.PreferenceDialogFragmentCompat;

public class AboutPreferenceDialogFragment extends PreferenceDialogFragmentCompat {

    public static AboutPreferenceDialogFragment newInstance(String key) {
        AboutPreferenceDialogFragment fragment = new AboutPreferenceDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_KEY, key);
        fragment.setArguments(arguments);
        return fragment;
    }

    private AboutPreferenceDialogFragment() {
        super();
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {

    }
}