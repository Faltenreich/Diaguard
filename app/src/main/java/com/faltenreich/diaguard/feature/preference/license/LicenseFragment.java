package com.faltenreich.diaguard.feature.preference.license;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentLicenseBinding;
import com.faltenreich.diaguard.feature.navigation.Navigating;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class LicenseFragment extends BaseFragment<FragmentLicenseBinding> implements Navigating, ToolbarDescribing {

    @Override
    protected FragmentLicenseBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentLicenseBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.licenses)
            .build();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addLicenses();
    }

    private void addLicenses() {
        LibsBuilder builder = new LibsBuilder().withFields(R.string.class.getFields());
        builder.setAboutShowIcon(false);
        builder.setAboutShowVersionName(false);
        builder.setAboutShowVersionCode(false);
        Fragment fragment = builder.supportFragment();
        openFragment(fragment, Navigation.Operation.REPLACE, false);
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, @NonNull Navigation.Operation operation, boolean addToBackStack) {
        Navigation.openFragment(fragment, getChildFragmentManager(), R.id.fragment_container, operation, addToBackStack);
    }
}
