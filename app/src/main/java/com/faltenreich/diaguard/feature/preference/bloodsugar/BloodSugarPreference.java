package com.faltenreich.diaguard.feature.preference.bloodsugar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.feature.preference.data.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
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

    String getValueForUi() {
        String value = getPersistedString(null);
        float number = FloatUtils.parseNumber(value);
        number = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, number);
        return FloatUtils.parseFloat(number);
    }

    void setValueFromUi(String valueFromUi) {
        float number = FloatUtils.parseNumber(valueFromUi);
        number = PreferenceHelper.getInstance().formatCustomToDefaultUnit(Category.BLOODSUGAR, number);
        String value = Float.toString(number);

        persistString(value);

        Events.post(new BloodSugarPreferenceChangedEvent());
    }
}