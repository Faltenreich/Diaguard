package com.faltenreich.diaguard.feature.preference.therapy;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.PreferenceFragment;
import com.faltenreich.diaguard.feature.preference.factor.FactorPreference;
import com.faltenreich.diaguard.feature.preference.factor.FactorPreferenceDialogFragment;

public class FactorPreferenceFragment extends PreferenceFragment {

    public FactorPreferenceFragment() {
        super(R.xml.preferences_factor, R.string.factors);
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
