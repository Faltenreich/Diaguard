package com.faltenreich.diaguard.feature.preference;

import android.app.Fragment;
import android.os.Bundle;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class PreferenceActivity extends BaseActivity {
    
    public PreferenceActivity() {
        super(R.layout.activity_preference);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(R.id.content, new PreferenceFragment()).commit();
    }

    public Fragment getFragment() {
        return getFragmentManager().findFragmentById(R.id.content);
    }
}