package com.faltenreich.diaguard.feature.entry.search;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;

public class EntrySearchActivity extends BaseActivity {

    private static Intent getIntent(Context context, @Nullable View source) {
        return BaseActivity.getIntent(EntrySearchActivity.class, context, source);
    }

    public static void show(Context context, @Nullable View source) {
        context.startActivity(getIntent(context, source));
    }

    public static void show(Context context, Tag tag) {
        Intent intent = getIntent(context, null);
        intent.putExtra(EntrySearchFragment.EXTRA_TAG_ID, tag.getId());
        context.startActivity(intent);
    }

    public EntrySearchActivity() {
        super(R.layout.activity_entry_search);
    }
}