package com.faltenreich.diaguard.feature.preference.cgm;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.cgm.xdrip.XDripBroadcastReceiver;
import com.faltenreich.diaguard.feature.preference.PreferenceFragment;

public class CgmPreferenceFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener {

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
            XDripBroadcastReceiver.invalidate(requireContext());
        }
    }
}
