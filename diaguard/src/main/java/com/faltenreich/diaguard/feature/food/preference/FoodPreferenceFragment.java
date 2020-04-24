package com.faltenreich.diaguard.feature.food.preference;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;

public class FoodPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_food);
    }
}
