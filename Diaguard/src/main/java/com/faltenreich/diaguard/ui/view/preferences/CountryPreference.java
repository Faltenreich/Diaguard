package com.faltenreich.diaguard.ui.view.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.faltenreich.diaguard.util.Helper;

/**
 * Created by Faltenreich on 08.11.2016.
 */

public class CountryPreference extends ListPreference {

    public CountryPreference(Context context) {
        super(context);
        init();
    }

    public CountryPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEntries(getEntries());
        setEntryValues(getEntryValues());
    }

    @Override
    public CharSequence[] getEntries() {
        return Helper.getCountryNames();
    }

    @Override
    public CharSequence[] getEntryValues() {
        return Helper.getCountryCodes();
    }
}
