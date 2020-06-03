package com.faltenreich.diaguard.feature.preference.license;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;

import com.faltenreich.diaguard.R;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class LicensePreference extends Preference {

    public LicensePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        super.onClick();
        openLicenses();
    }

    private void openLicenses() {
        LibsBuilder builder = new LibsBuilder().withFields(R.string.class.getFields());
        builder.setActivityTitle(getContext().getString(R.string.licenses));
        builder.setAboutShowIcon(false);
        builder.setAboutShowVersionName(false);
        builder.setAboutShowVersionCode(false);
        builder.start(getContext());
    }
}