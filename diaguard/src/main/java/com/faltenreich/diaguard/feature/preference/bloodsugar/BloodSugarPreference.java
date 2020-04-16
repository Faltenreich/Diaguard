package com.faltenreich.diaguard.feature.preference.bloodsugar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.preference.BloodSugarPreferenceChangedEvent;

public class BloodSugarPreference extends EditTextPreference {

    public BloodSugarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.preference_bloodsugar;
    }

    String getValue() {
        return getPersistedString(null);
    }

    void setValue(String value) {
        persistString(value);
        Events.post(new BloodSugarPreferenceChangedEvent());
    }
}