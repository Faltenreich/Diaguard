package com.faltenreich.diaguard.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.ui.fragments.EntryDetailFragment;
import com.faltenreich.diaguard.util.ViewHelper;

import org.joda.time.format.DateTimeFormat;


public class EntryDetailActivity extends BaseActivity {

    private long entryId;

    public EntryDetailActivity() {
        super(R.layout.activity_entry_detail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = new Bundle();
        entryId = getIntent().getLongExtra(EntryDetailFragment.EXTRA_ENTRY, 0);
        arguments.putLong(EntryDetailFragment.EXTRA_ENTRY, entryId);
        EntryDetailFragment fragment = new EntryDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.entry_detail, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        setDateTime();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setDateTime();
    }

    private void setDateTime() {
        Entry entry = EntryDao.getInstance().get(entryId);
        if (entry != null && toolbar != null){
            boolean showShortText = !ViewHelper.isLandscape(this) && !ViewHelper.isLargeScreen(this);

            String weekDay = showShortText ?
                    entry.getDate().dayOfWeek().getAsShortText() :
                    entry.getDate().dayOfWeek().getAsText();
            String date = showShortText ?
                    DateTimeFormat.shortDate().print(entry.getDate()) :
                    DateTimeFormat.mediumDate().print(entry.getDate());
            String time = DateTimeFormat.forPattern("HH:mm").print(entry.getDate());
            
            setTitle(String.format("%s, %s %s", weekDay, date, time));
        }
    }
}
