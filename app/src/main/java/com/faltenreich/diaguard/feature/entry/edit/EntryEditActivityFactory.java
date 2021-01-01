package com.faltenreich.diaguard.feature.entry.edit;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Food;

import org.joda.time.DateTime;

public class EntryEditActivityFactory {

    private static Intent getIntent(Context context) {
        return new Intent(context, EntryEditActivity.class);
    }

    public static Intent newInstance(Context context) {
        return getIntent(context);
    }

    public static Intent newInstance(Context context, @Nullable Entry entry) {
        Intent intent = getIntent(context);
        if (entry != null) {
            intent.putExtra(EntryEditActivity.EXTRA_ENTRY_ID, entry.getId());
        }
        return intent;
    }

    public static Intent newInstance(Context context, @Nullable Food food) {
        Intent intent = getIntent(context);
        if (food != null) {
            intent.putExtra(EntryEditActivity.EXTRA_FOOD_ID, food.getId());
        }
        return intent;
    }

    public static Intent newInstance(Context context, @NonNull DateTime dateTime) {
        Intent intent = getIntent(context);
        intent.putExtra(EntryEditActivity.EXTRA_DATE, dateTime);
        return intent;
    }

    public static Intent newInstance(Context context, @NonNull Category category) {
        Intent intent = getIntent(context);
        intent.putExtra(EntryEditActivity.EXTRA_CATEGORY, category);
        return intent;
    }
}
