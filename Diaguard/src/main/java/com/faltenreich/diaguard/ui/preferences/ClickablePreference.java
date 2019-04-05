package com.faltenreich.diaguard.ui.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.event.BaseEvent;
import com.faltenreich.diaguard.event.Events;

abstract class ClickablePreference <EVENT extends BaseEvent> extends Preference implements Preference.OnPreferenceClickListener {

    public ClickablePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Events.post(getEvent());
        return true;
    }

    abstract EVENT getEvent();
}