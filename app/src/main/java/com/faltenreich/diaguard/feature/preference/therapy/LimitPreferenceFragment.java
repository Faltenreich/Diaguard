package com.faltenreich.diaguard.feature.preference.therapy;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.PreferenceFragment;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

import java.util.Locale;

public class LimitPreferenceFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    public LimitPreferenceFragment() {
        super(R.xml.preferences_limit, R.string.pref_therapy_limits);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        invalidateEnabledStates();
        setSummaries();
    }

    @Override
    public void onDestroyView() {
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_extrema_highlight))) {
            invalidateEnabledStates();
        }
    }

    private void invalidateEnabledStates() {
        boolean isEnabled = PreferenceStore.getInstance().limitsAreHighlighted();
        requirePreference(getString(R.string.preference_extrema_hyperclycemia)).setEnabled(isEnabled);
        requirePreference(getString(R.string.preference_extrema_hypoclycemia)).setEnabled(isEnabled);
        requirePreference(getString(R.string.preference_extrema_target)).setEnabled(isEnabled);
    }

    private void setSummaries() {
        setSummaryForHyperglycemia();
        setSummaryForHypoglycemia();
        setSummaryForTarget();
    }

    private String getSummaryForLimit(float limit) {
        float custom = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, limit);
        return String.format(
            Locale.getDefault(),
            "%s %s",
            FloatUtils.parseFloat(custom),
            PreferenceStore.getInstance().getUnitAcronym(Category.BLOODSUGAR)
        );
    }

    private void setSummaryForHyperglycemia() {
        Preference preference = requirePreference(getString(R.string.preference_extrema_hyperclycemia));
        preference.setSummaryProvider(pref ->
            getSummaryForLimit(PreferenceStore.getInstance().getLimitHyperglycemia())
        );
    }

    private void setSummaryForHypoglycemia() {
        Preference preference = requirePreference(getString(R.string.preference_extrema_hypoclycemia));
        preference.setSummaryProvider(pref ->
            getSummaryForLimit(PreferenceStore.getInstance().getLimitHypoglycemia())
        );
    }

    private void setSummaryForTarget() {
        Preference preference = requirePreference(getString(R.string.preference_extrema_target));
        preference.setSummaryProvider(pref ->
            getSummaryForLimit(PreferenceStore.getInstance().getTargetValue())
        );
    }
}
