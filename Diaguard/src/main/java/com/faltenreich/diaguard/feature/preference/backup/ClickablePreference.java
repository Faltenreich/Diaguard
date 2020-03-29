package com.faltenreich.diaguard.feature.preference.backup;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.shared.event.BaseEvent;
import com.faltenreich.diaguard.shared.event.Events;

abstract class ClickablePreference <EVENT extends BaseEvent> extends Preference implements Preference.OnPreferenceClickListener {

    ClickablePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Events.post(getEvent());
        return true;
    }

    public abstract EVENT getEvent();
}