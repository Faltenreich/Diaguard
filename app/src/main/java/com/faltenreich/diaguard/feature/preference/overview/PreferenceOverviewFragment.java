package com.faltenreich.diaguard.feature.preference.overview;

import android.os.Bundle;

import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.PreferenceFragment;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.SystemUtils;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.event.preference.DecimalPlacesChangedEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PreferenceOverviewFragment extends PreferenceFragment {

    public PreferenceOverviewFragment() {
        super(R.xml.preferences_overview, R.string.settings);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        setSummaryForDecimalPlaces();
        setSummaryForVersion();
    }

    private void setSummaryForDecimalPlaces() {
        Preference preference = requirePreference(getString(R.string.preference_decimal_places));
        preference.setSummaryProvider(
            pref -> {
                int decimalPlaces = PreferenceStore.getInstance().getDecimalPlaces();
                return String.format("%s: %d, e.g. %s",
                    getString(R.string.decimal_places_desc),
                    decimalPlaces,
                    FloatUtils.parseFloat(1.23456789f, decimalPlaces)
                );
            }
        );
    }

    private void setSummaryForVersion() {
        Preference preference = requirePreference(getString(R.string.preference_version));
        preference.setSummaryProvider(pref -> SystemUtils.getVersionName(requireActivity()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DecimalPlacesChangedEvent event) {
        setSummaryForDecimalPlaces();
    }
}