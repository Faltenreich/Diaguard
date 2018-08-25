package com.faltenreich.diaguard.ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.fragment.PreferenceFragment;

public class PreferenceActivity extends BaseActivity {

    private static final @IdRes int CONTENT_ID = R.id.content;
    
    public PreferenceActivity() {
        super(R.layout.activity_preferences);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(CONTENT_ID, new PreferenceFragment()).commit();
    }

    public Fragment getFragment() {
        return getFragmentManager().findFragmentById(CONTENT_ID);
    }
}