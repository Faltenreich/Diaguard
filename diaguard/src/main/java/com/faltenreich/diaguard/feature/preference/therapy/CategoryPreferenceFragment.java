package com.faltenreich.diaguard.feature.preference.therapy;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;

public class CategoryPreferenceFragment extends PreferenceFragmentCompat {

    public CategoryPreferenceFragment() {
        super();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_category);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(R.string.categories);
    }
}
