package com.faltenreich.diaguard;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.faltenreich.diaguard.helpers.ViewHelper;

/**
 * Created by Filip on 27.05.2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set the padding to match the Status Bar height
        toolbar.setPadding(0, ViewHelper.getStatusBarHeight(this), 0, 0);

        // TODO: Set the padding of NavigationView header to match the Status Bar height
    }
}
