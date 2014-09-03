package com.faltenreich.diaguard;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class EntryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
