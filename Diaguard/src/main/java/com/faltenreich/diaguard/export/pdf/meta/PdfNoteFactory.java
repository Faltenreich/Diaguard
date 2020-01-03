package com.faltenreich.diaguard.export.pdf.meta;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PdfNoteFactory {

    @Nullable
    public static PdfNote createNote(PdfExportConfig config, Entry entry) {
        List<String> entryNotesAndTagsOfDay = new ArrayList<>();
        List<String> foodOfDay = new ArrayList<>();
        if (config.isExportNotes() && !StringUtils.isBlank(entry.getNote())) {
            entryNotesAndTagsOfDay.add(entry.getNote());
        }
        if (config.isExportTags()) {
            List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
            for (EntryTag entryTag : entryTags) {
                Tag tag = entryTag.getTag();
                if (tag != null) {
                    entryNotesAndTagsOfDay.add(entryTag.getTag().getName());
                }
            }
        }
        if (config.isExportFood()) {
            Meal meal = (Meal) MeasurementDao.getInstance(Meal.class).getMeasurement(entry);
            if (meal != null) {
                for (FoodEaten foodEaten : FoodEatenDao.getInstance().getAll(meal)) {
                    String foodNote = foodEaten.print();
                    if (foodNote != null) {
                        foodOfDay.add(foodNote);
                    }
                }
            }
        }
        boolean hasEntryNotesAndTags = !entryNotesAndTagsOfDay.isEmpty();
        boolean hasFood = !foodOfDay.isEmpty();
        if (hasEntryNotesAndTags || hasFood) {
            List<String> notes = new ArrayList<>();
            if (hasEntryNotesAndTags) {
                notes.add(TextUtils.join(", ", entryNotesAndTagsOfDay));
            }
            if (hasFood) {
                notes.add(TextUtils.join(", ", foodOfDay));
            }
            String note = TextUtils.join("\n", notes);
            return new PdfNote(entry.getDate(), note);
        }
        return null;
    }
}
