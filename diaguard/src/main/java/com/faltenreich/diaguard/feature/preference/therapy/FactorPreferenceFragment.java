package com.faltenreich.diaguard.feature.preference.therapy;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;

public class FactorPreferenceFragment extends PreferenceFragmentCompat {

    public FactorPreferenceFragment() {
        super();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_factor);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(R.string.factors);
    }
}
