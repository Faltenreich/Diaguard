package com.faltenreich.diaguard.feature.preference;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.XmlRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;

import com.faltenreich.diaguard.shared.view.resource.ColorUtils;

public abstract class PreferenceFragment extends PreferenceFragmentCompat {

    @XmlRes private int preferenceRes;
    @StringRes private int titleRes;

    public PreferenceFragment(@XmlRes int preferenceRes, @StringRes int titleRes) {
        this.preferenceRes = preferenceRes;
        this.titleRes = titleRes;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(preferenceRes);
        applyTheme();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(titleRes);
    }

    protected SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(requireActivity());
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

    @NonNull
    protected Preference requirePreference(String key) {
        Preference preference = findPreference(key);
        if (preference == null) {
            throw new IllegalStateException("Preference with key " + key + " not found.");
        }
        return preference;
    }
}
