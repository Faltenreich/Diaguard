package com.faltenreich.diaguard.feature.preference.therapy;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.BasePreferenceFragment;
import com.faltenreich.diaguard.feature.preference.bloodsugar.BloodSugarPreference;
import com.faltenreich.diaguard.feature.preference.bloodsugar.BloodSugarPreferenceDialogFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;

public class LimitPreferenceFragment extends BasePreferenceFragment {

    public LimitPreferenceFragment() {
        super(R.xml.preferences_limit, R.string.pref_therapy_limits);
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
    protected void onSummarySet(Preference preference) {
        super.onSummarySet(preference);

        if (preference instanceof BloodSugarPreference) {
            String value = PreferenceHelper.getInstance().getValueForKey(preference.getKey());
            float number = FloatUtils.parseNumber(value);
            if (number > 0) {
                if (getActivity() != null) {
                    int descriptionResId = getResources().getIdentifier(preference.getKey() + "_desc", "string", getActivity().getPackageName());
                    String description = descriptionResId > 0 ? getString(descriptionResId) + " " : "";
                    number = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, number);
                    value = FloatUtils.parseFloat(number);
                    preference.setSummary(description + value + " " + PreferenceHelper.getInstance().getUnitAcronym(Category.BLOODSUGAR));
                }
            } else {
                preference.setSummary(null);
            }

        }
    }
}
