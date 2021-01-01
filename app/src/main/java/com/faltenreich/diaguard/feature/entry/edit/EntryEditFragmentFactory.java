package com.faltenreich.diaguard.feature.entry.edit;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Food;

import org.joda.time.DateTime;

public class EntryEditFragmentFactory {

    public static EntryEditFragment newInstance(@NonNull Entry entry) {
        EntryEditFragment fragment = new EntryEditFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(EntryEditFragment.EXTRA_ENTRY_ID, entry.getId());
        fragment.setArguments(arguments);
        return fragment;
    }

    public static EntryEditFragment newInstance(@NonNull Food food) {
        EntryEditFragment fragment = new EntryEditFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(EntryEditFragment.EXTRA_FOOD_ID, food.getId());
        fragment.setArguments(arguments);
        return fragment;
    }

    public static EntryEditFragment newInstance(@NonNull DateTime dateTime) {
        EntryEditFragment fragment = new EntryEditFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(EntryEditFragment.EXTRA_DATE, dateTime);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static EntryEditFragment newInstance(@NonNull Category category) {
        EntryEditFragment fragment = new EntryEditFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(EntryEditFragment.EXTRA_CATEGORY, category);
        fragment.setArguments(arguments);
        return fragment;
    }
}
