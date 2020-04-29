package com.faltenreich.diaguard.feature.preference;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.annotation.XmlRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.faltenreich.diaguard.shared.view.resource.ColorUtils;

import java.util.ArrayList;

public abstract class BasePreferenceFragment
    extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener
{

    @XmlRes private int preferenceRes;
    @StringRes private int titleRes;

    public BasePreferenceFragment(@XmlRes int preferenceRes, @StringRes int titleRes) {
        this.preferenceRes = preferenceRes;
        this.titleRes = titleRes;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(preferenceRes);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        applyTheme();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(titleRes);
        setSummaries();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSummary(findPreference(key));
    }

    private void applyTheme() {
        int color = ColorUtils.getPrimaryColor(getContext());
        applyThemeToIcons(getPreferenceScreen(), color);
    }

    private void applyThemeToIcons(Preference preference, int color) {
        Drawable icon = preference.getIcon();

        if (icon != null) {
            DrawableCompat.setTint(icon, color);
            preference.setIcon(icon);
        }

        if (preference instanceof PreferenceGroup) {
            PreferenceGroup group = ((PreferenceGroup) preference);
            for (int index = 0; index < group.getPreferenceCount(); index++) {
                applyThemeToIcons(group.getPreference(index), color);
            }
        }
    }

    private ArrayList<Preference> getPreferenceList(Preference preference, ArrayList<Preference> list) {
        if (preference instanceof PreferenceCategory || preference instanceof PreferenceScreen) {
            PreferenceGroup group = (PreferenceGroup) preference;
            for (int index = 0; index < group.getPreferenceCount(); index++) {
                getPreferenceList(group.getPreference(index), list);
            }
        } else {
            list.add(preference);
        }
        return list;
    }

    private void setSummaries() {
        for (Preference preference : getPreferenceList(getPreferenceScreen(), new ArrayList<>())) {
            setSummary(preference);
        }
    }

    private void setSummary(final Preference preference) {
        if (isAdded() && preference != null) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());
            }
            onSummarySet(preference);
        }
    }

    protected void onSummarySet(Preference preference) {

    }
}
