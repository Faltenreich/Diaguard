package com.faltenreich.diaguard.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.fragment.EntrySearchFragment;

public class EntrySearchActivity extends BaseSearchActivity {

    public static Intent newInstance(Context context, Tag tag) {
        Intent intent = new Intent(context, EntrySearchActivity.class);
        intent.putExtra(EntrySearchFragment.EXTRA_TAG_ID, tag.getId());
        return intent;
    }

    public EntrySearchActivity() {
        super(R.layout.activity_entry_search);
    }
}