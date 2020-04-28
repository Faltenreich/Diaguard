package com.faltenreich.diaguard.feature.preference.therapy;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;

public class UnitPreferenceFragment extends PreferenceFragmentCompat {

    public UnitPreferenceFragment() {
        super();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_unit);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(R.string.units);
    }
}
