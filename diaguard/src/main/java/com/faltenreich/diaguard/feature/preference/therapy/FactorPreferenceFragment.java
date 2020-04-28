package com.faltenreich.diaguard.feature.preference.therapy;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.factor.FactorPreference;
import com.faltenreich.diaguard.feature.preference.factor.FactorPreferenceDialogFragment;

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

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment fragment = null;
        if (preference instanceof FactorPreference) {
            fragment = FactorPreferenceDialogFragment.newInstance(preference.getKey());
        }
        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
            fragment.show(getParentFragmentManager(), fragment.getClass().getName());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
