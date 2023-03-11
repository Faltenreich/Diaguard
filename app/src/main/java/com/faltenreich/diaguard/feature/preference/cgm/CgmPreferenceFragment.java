package com.faltenreich.diaguard.feature.preference.cgm;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.cgm.xdrip.xDripRepository;
import com.faltenreich.diaguard.feature.preference.PreferenceFragment;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;

public class CgmPreferenceFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final PreferenceStore preferenceStore = PreferenceStore.getInstance();
    private final xDripRepository xDripRepository = new xDripRepository();

    public CgmPreferenceFragment() {
        super(R.xml.preferences_cgm, R.string.cgm);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_cgm_xdrip))) {
            boolean shouldReadCgmDataFromXDrip = preferenceStore.shouldReadCgmDataFromXDrip();
            xDripRepository.toggleBroadcastReceiver(requireContext(), shouldReadCgmDataFromXDrip);
        }
    }
}
