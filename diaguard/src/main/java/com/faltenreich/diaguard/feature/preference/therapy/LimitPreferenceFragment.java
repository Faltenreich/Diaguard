package com.faltenreich.diaguard.feature.preference.therapy;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;

public class LimitPreferenceFragment extends PreferenceFragmentCompat {

    public LimitPreferenceFragment() {
        super();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_limit);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(R.string.pref_therapy_limits);
    }
}
