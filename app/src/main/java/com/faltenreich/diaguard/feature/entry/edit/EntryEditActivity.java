package com.faltenreich.diaguard.feature.entry.edit;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityEntryEditBinding;
import com.faltenreich.diaguard.feature.navigation.Navigating;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class EntryEditActivity extends BaseActivity<ActivityEntryEditBinding> implements Navigating {

    public EntryEditActivity() {
        super(R.layout.activity_entry_edit);
    }

    @Override
    protected ActivityEntryEditBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityEntryEditBinding.inflate(layoutInflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment();
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, @NonNull Navigation.Operation operation, boolean addToBackStack) {
        Navigation.openFragment(fragment, getSupportFragmentManager(), R.id.fragment_container, operation, addToBackStack);
    }

    private void addFragment() {
        Fragment fragment = new EntryEditFragment();
        fragment.setArguments(getIntent().getExtras());
        openFragment(fragment, Navigation.Operation.REPLACE, false);
    }
}