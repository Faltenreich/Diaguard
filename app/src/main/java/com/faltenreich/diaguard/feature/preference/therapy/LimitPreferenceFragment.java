package com.faltenreich.diaguard.feature.preference.therapy;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.BasePreferenceFragment;
import com.faltenreich.diaguard.feature.preference.bloodsugar.BloodSugarPreference;
import com.faltenreich.diaguard.feature.preference.bloodsugar.BloodSugarPreferenceDialogFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

import java.util.Locale;

public class LimitPreferenceFragment extends BasePreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_LIMITS_HIGHLIGHTED = "targets_highlight";
    private static final String KEY_HYPERGLYCEMIA = "hyperclycemia";
    private static final String KEY_HYPOGLYCEMIA = "hypoclycemia";
    private static final String KEY_TARGET = "target";

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
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment fragment = null;
        if (preference instanceof BloodSugarPreference) {
            fragment = BloodSugarPreferenceDialogFragment.newInstance(preference.getKey());
        }
        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
            fragment.show(getParentFragmentManager(), fragment.getClass().getName());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_LIMITS_HIGHLIGHTED)) {
            invalidateEnabledStates();
        }
    }

    private void invalidateEnabledStates() {
        boolean isEnabled = PreferenceHelper.getInstance().limitsAreHighlighted();
        requirePreference(KEY_HYPERGLYCEMIA).setEnabled(isEnabled);
        requirePreference(KEY_HYPOGLYCEMIA).setEnabled(isEnabled);
        requirePreference(KEY_TARGET).setEnabled(isEnabled);
    }

    private void setSummaries() {
        setSummaryForHyperglycemia();
        setSummaryForHypoglycemia();
        setSummaryForTarget();
    }

    private String getSummaryForLimit(float limit) {
        float custom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, limit);
        return String.format(
            Locale.getDefault(),
            "%s %s",
            FloatUtils.parseFloat(custom),
            PreferenceHelper.getInstance().getUnitAcronym(Category.BLOODSUGAR)
        );
    }

    private void setSummaryForHyperglycemia() {
        Preference preference = requirePreference(KEY_HYPERGLYCEMIA);
        preference.setSummaryProvider(pref ->
            getSummaryForLimit(PreferenceHelper.getInstance().getLimitHyperglycemia())
        );
    }

    private void setSummaryForHypoglycemia() {
        Preference preference = requirePreference(KEY_HYPOGLYCEMIA);
        preference.setSummaryProvider(pref ->
            getSummaryForLimit(PreferenceHelper.getInstance().getLimitHypoglycemia())
        );
    }

    private void setSummaryForTarget() {
        Preference preference = requirePreference(KEY_TARGET);
        preference.setSummaryProvider(pref ->
            getSummaryForLimit(PreferenceHelper.getInstance().getTargetValue())
        );
    }
}
