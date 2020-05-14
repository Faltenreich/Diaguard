package com.faltenreich.diaguard.feature.preference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.food.FoodPreferenceFragment;
import com.faltenreich.diaguard.feature.preference.overview.PreferenceOverviewFragment;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

import java.io.Serializable;

public class PreferenceActivity extends BaseActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String ARGUMENT_LINK = "link";

    public enum Link {
        NONE,
        FOOD
    }

    public static Intent newInstance(Context context, Link link) {
        Intent intent = new Intent(context, PreferenceActivity.class);
        intent.putExtra(ARGUMENT_LINK, link);
        return intent;
    }

    public PreferenceActivity() {
        super(R.layout.activity_preference);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragment(getInitialFragment(), false);
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference preference) {
        Bundle arguments = preference.getExtras();
        FragmentFactory factory = getSupportFragmentManager().getFragmentFactory();
        Fragment fragment = factory.instantiate(getClassLoader(), preference.getFragment());
        fragment.setArguments(arguments);
        fragment.setTargetFragment(caller, 0);
        setFragment(fragment, true);
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

    private Fragment getInitialFragment() {
        Link link;

        Serializable argument = getIntent().getSerializableExtra(ARGUMENT_LINK);
        if (argument instanceof Link) {
            link = (Link) argument;
        } else {
            link = Link.NONE;
        }

        if (link == Link.FOOD) {
            return new FoodPreferenceFragment();
        } else {
            return new PreferenceOverviewFragment();
        }
    }

    private void setFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content, fragment);
        if (addToBackStack) {
            transaction = transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }
}