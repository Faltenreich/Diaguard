package com.faltenreich.diaguard;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.fragments.EntryDetailFragment;
import com.faltenreich.diaguard.helpers.Helper;


public class EntryDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        Bundle arguments = new Bundle();
        long id = getIntent().getLongExtra(EntryDetailFragment.ENTRY_ID, 0);
        arguments.putLong(EntryDetailFragment.ENTRY_ID, id);
        EntryDetailFragment fragment = new EntryDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.entry_detail, fragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            DatabaseDataSource dataSource = new DatabaseDataSource(this);
            dataSource.open();
            Entry entry = (Entry) dataSource.get(DatabaseHelper.ENTRY, id);
            dataSource.close();
            toolbar.setTitle(
                    Helper.getDateFormat().print(entry.getDate()) + " " +
                    Helper.getTimeFormat().print(entry.getDate()));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setSupportActionBar(toolbar);
        }
    }
}
