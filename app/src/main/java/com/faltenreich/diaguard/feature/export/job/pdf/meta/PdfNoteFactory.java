package com.faltenreich.diaguard.feature.export.job.pdf.meta;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.dao.EntryTagOrmLiteDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PdfNoteFactory {

    @Nullable
    public static PdfNote createNote(PdfExportConfig config, Entry entry) {
        List<String> entryNotesAndTagsOfDay = new ArrayList<>();
        List<String> foodOfDay = new ArrayList<>();
        if (config.exportNotes() && !StringUtils.isBlank(entry.getNote())) {
            entryNotesAndTagsOfDay.add(entry.getNote());
        }
        if (config.exportTags()) {
            List<EntryTag> entryTags = EntryTagOrmLiteDao.getInstance().getByEntry(entry);
            for (EntryTag entryTag : entryTags) {
                Tag tag = entryTag.getTag();
                if (tag != null) {
                    entryNotesAndTagsOfDay.add(entryTag.getTag().getName());
                }
            }
        }
        if (config.exportFood()) {
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
