package com.faltenreich.diaguard.feature.preference;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class PreferenceActivity extends BaseActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    public PreferenceActivity() {
        super(R.layout.activity_preference);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content, new PreferenceFragment())
            .commit();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference preference) {
        Bundle arguments = preference.getExtras();
        FragmentFactory factory = getSupportFragmentManager().getFragmentFactory();
        Fragment fragment = factory.instantiate(getClassLoader(), preference.getFragment());
        fragment.setArguments(arguments);
        fragment.setTargetFragment(caller, 0);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.content, fragment)
            .addToBackStack(null)
            .commit();
        setTitle(preference.getTitle());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}