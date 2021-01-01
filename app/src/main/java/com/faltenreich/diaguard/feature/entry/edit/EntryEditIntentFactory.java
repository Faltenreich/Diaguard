package com.faltenreich.diaguard.feature.entry.edit;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;

import org.joda.time.DateTime;

@Deprecated
public class EntryEditIntentFactory {

    public static Intent newInstance(Context context, @Nullable Entry entry) {
        Intent intent = new Intent(context, EntryEditActivity.class);
        if (entry != null) {
            intent.putExtra(EntryEditFragment.EXTRA_ENTRY_ID, entry.getId());
        }
        return intent;
    }

    public static Intent newInstance(Context context, @NonNull DateTime dateTime) {
        Intent intent = new Intent(context, EntryEditActivity.class);
        intent.putExtra(EntryEditFragment.EXTRA_DATE, dateTime);
        return intent;
    }
}
