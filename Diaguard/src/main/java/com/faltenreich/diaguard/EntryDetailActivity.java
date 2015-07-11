package com.faltenreich.diaguard;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.fragments.EntryDetailFragment;
import com.faltenreich.diaguard.helpers.Helper;


public class EntryDetailActivity extends BaseActivity {

    public static final String EXTRA_ENTRY = "com.faltenreich.diaguard.EntryDetailActivity.ENTRY";

    private Entry entry;
    private Toolbar toolbar;

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

        //entry = (Entry) dataSource.get(DatabaseHelper.ENTRY, id);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (entry != null && toolbar != null){
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setSupportActionBar(toolbar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update entry
        //entry = (Entry) dataSource.get(DatabaseHelper.ENTRY, this.entry.getId());

        // Display date and time of entry in ActionBar
        if (entry != null && toolbar != null){
            toolbar.setTitle(Helper.getDateFormat().print(entry.getDate()) + " " +
                    Helper.getTimeFormat().print(entry.getDate()));
        }
    }
}
