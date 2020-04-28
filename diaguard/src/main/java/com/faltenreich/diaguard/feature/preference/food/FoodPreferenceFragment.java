package com.faltenreich.diaguard.feature.preference.food;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;

public class FoodPreferenceFragment extends PreferenceFragmentCompat {

    public FoodPreferenceFragment() {
        super();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_food);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(R.string.food);
    }
}
