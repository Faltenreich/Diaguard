package com.faltenreich.diaguard;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.helpers.ViewHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Filip on 27.05.2015.
 */
public class BaseActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onStart() {
        super.onStart();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            // Set the padding to match the Status Bar height
            toolbar.setPadding(0, ViewHelper.getStatusBarHeight(this), 0, 0);

            // TODO: Set the padding of NavigationView header to match the Status Bar height
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
