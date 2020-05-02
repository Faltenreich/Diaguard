package com.faltenreich.diaguard.feature.preference.license;

import android.content.Context;

import com.faltenreich.diaguard.R;

import de.psdev.licensesdialog.licenses.License;

/**
 * Created by Faltenreich on 25.11.2016.
 */

public class OpenDatabaseLicense extends License {

    @Override
    public String getName() {
        return "Open Database License";
    }

    @Override
    public String readSummaryTextFromResources(Context context) {
        return getContent(context, R.raw.odbl_summary);
    }

    @Override
    public String readFullTextFromResources(Context context) {
        return getContent(context, R.raw.odbl_full);
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getUrl() {
        return "http://opendatacommons.org/licenses/odbl/1.0/";
    }
}
