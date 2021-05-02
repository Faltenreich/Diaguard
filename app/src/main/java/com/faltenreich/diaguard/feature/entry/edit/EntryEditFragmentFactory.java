package com.faltenreich.diaguard.feature.entry.edit;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Food;

import org.joda.time.DateTime;

public class EntryEditFragmentFactory {

    public static EntryEditFragment newInstance(@Nullable Entry entry) {
        EntryEditFragment fragment = new EntryEditFragment();
        if (entry != null) {
            Bundle arguments = new Bundle();
            arguments.putLong(EntryEditViewModel.EXTRA_ENTRY_ID, entry.getId());
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public static EntryEditFragment newInstance(@Nullable Food food) {
        EntryEditFragment fragment = new EntryEditFragment();
        if (food != null) {
            Bundle arguments = new Bundle();
            arguments.putLong(EntryEditViewModel.EXTRA_FOOD_ID, food.getId());
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public static EntryEditFragment newInstance(@Nullable DateTime dateTime) {
        EntryEditFragment fragment = new EntryEditFragment();
        if (dateTime != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(EntryEditViewModel.EXTRA_DATE, dateTime);
            fragment.setArguments(arguments);
        }
        return fragment;
    }
}
