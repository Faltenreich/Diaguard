package com.faltenreich.diaguard.feature.preference.overview;

import android.os.Bundle;

import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.config.ApplicationConfig;
import com.faltenreich.diaguard.feature.preference.PreferenceFragment;
import com.faltenreich.diaguard.shared.SystemUtils;

public class PreferenceOverviewFragment extends PreferenceFragment {

    public PreferenceOverviewFragment() {
        super(R.xml.preferences_overview, R.string.settings);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        setSummaryForVersion();
        hideCgmIfNeeded();
    }

    private void setSummaryForVersion() {
        Preference preference = requirePreference(getString(R.string.preference_version));
        preference.setSummaryProvider(pref -> SystemUtils.getVersionName(requireActivity()));
    }

    private void hideCgmIfNeeded() {
        Preference preference = requirePreference(getString(R.string.preference_cgm));
        preference.setVisible(ApplicationConfig.isCgmSupported());
    }
}