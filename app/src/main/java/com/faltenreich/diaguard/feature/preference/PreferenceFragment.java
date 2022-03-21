package com.faltenreich.diaguard.feature.preference;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.XmlRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.navigation.OnFragmentChangeListener;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.backup.Backup;
import com.faltenreich.diaguard.feature.preference.bloodsugar.BloodSugarPreference;
import com.faltenreich.diaguard.feature.preference.bloodsugar.BloodSugarPreferenceDialogFragment;
import com.faltenreich.diaguard.feature.preference.data.PreferenceCache;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.decimal.DecimalPlacesPreference;
import com.faltenreich.diaguard.feature.preference.decimal.DecimalPlacesPreferenceDialogFragment;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.file.FileProvidedEvent;
import com.faltenreich.diaguard.shared.event.file.FileProvidedFailedEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.shared.event.preference.MealFactorUnitChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.TimelinePreferenceChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;
import com.faltenreich.diaguard.shared.view.theme.Theme;
import com.faltenreich.diaguard.shared.view.theme.ThemeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class PreferenceFragment
    extends PreferenceFragmentCompat
    implements ToolbarDescribing, SharedPreferences.OnSharedPreferenceChangeListener {

    private final int preferenceRes;
    private final int titleRes;


    public PreferenceFragment(@XmlRes int preferenceRes, @StringRes int titleRes) {
        this.preferenceRes = preferenceRes;
        this.titleRes = titleRes;
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), titleRes)
            .build();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(preferenceRes);
        applyTheme();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof OnFragmentChangeListener) {
            ((OnFragmentChangeListener) getActivity()).onFragmentChanged(this);
        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment fragment = null;
        if (preference instanceof BloodSugarPreference) {
            fragment = BloodSugarPreferenceDialogFragment.newInstance(preference.getKey());
        } else if (preference instanceof DecimalPlacesPreference) {
            fragment = DecimalPlacesPreferenceDialogFragment.newInstance(preference);
        }
        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
            fragment.show(getParentFragmentManager(), fragment.getClass().getName());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        Events.unregister(this);
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!isAdded()) {
            return;
        }
        if (key.equals(getString(R.string.preference_unit_bloodsugar))) {
            Events.post(new UnitChangedEvent(Category.BLOODSUGAR));
        } else if (key.equals(getString(R.string.preference_unit_meal))) {
            Events.post(new UnitChangedEvent(Category.MEAL));
        } else if (key.equals(getString(R.string.preference_unit_meal_factor))) {
            Events.post(new MealFactorUnitChangedEvent());
        } else if (key.equals(getString(R.string.preference_unit_hba1c))) {
            Events.post(new UnitChangedEvent(Category.HBA1C));
        } else if (key.equals(getString(R.string.preference_unit_weight))) {
            Events.post(new UnitChangedEvent(Category.WEIGHT));
        } else if (key.equals(getString(R.string.preference_timeline_show_dots))
            || key.equals(getString(R.string.preference_timeline_show_lines))) {
            Events.post(new TimelinePreferenceChangedEvent());
        } else if (key.equals(getString(R.string.preference_theme))) {
            Theme theme = PreferenceStore.getInstance().getTheme();
            ThemeUtils.setDefaultNightMode(theme);
            ThemeUtils.setUiMode(getActivity(), theme);
        } else if (key.equals(getString(R.string.preference_decimal_places))) {
            int decimalPlaces = PreferenceStore.getInstance().getDecimalPlaces();
            PreferenceCache.getInstance().setDecimalPlaces(decimalPlaces);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileProvidedEvent event) {
        new Backup().importBackup(requireActivity(), event.context);
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileProvidedFailedEvent event) {
        ViewUtils.showToast(getActivity(), getString(R.string.error_unexpected));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE && event.isGranted) {
            switch (event.useCase) {
                case BACKUP_WRITE:
                    new Backup().exportBackup(requireActivity());
                    break;
                case BACKUP_READ:
                    new Backup().searchBackups(requireActivity());
                    break;
            }
        }
    }
}
