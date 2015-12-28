package com.faltenreich.diaguard.ui.view.preferences;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;

import de.psdev.licensesdialog.LicensesDialog;

/**
 * Created by Filip on 04.11.13.
 */
public class LicensePreference extends Preference {

    public LicensePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        super.onClick();

        new LicensesDialog.Builder(getContext())
                .setNotices(R.raw.licenses)
                .setTitle(R.string.licenses)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }
}