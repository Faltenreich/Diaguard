package com.faltenreich.diaguard;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.faltenreich.diaguard.fragments.EntryDetailFragment;


public class EntryDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            long id = getIntent().getLongExtra(EntryDetailFragment.ENTRY_ID, 0);
            arguments.putLong(EntryDetailFragment.ENTRY_ID, id);
            EntryDetailFragment fragment = new EntryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.entry_detail, fragment)
                    .commit();
        }
    }
}
