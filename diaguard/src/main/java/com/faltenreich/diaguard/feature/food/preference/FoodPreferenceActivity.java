package com.faltenreich.diaguard.feature.food.preference;

import android.app.Fragment;
import android.os.Bundle;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class FoodPreferenceActivity extends BaseActivity {

    public FoodPreferenceActivity() {
        super(R.layout.activity_preference);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content, new FoodPreferenceFragment())
            .commit();
    }

    public Fragment getFragment() {
        return getFragmentManager().findFragmentById(R.id.content);
    }
}