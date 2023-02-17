package com.faltenreich.diaguard.feature.export.job.pdf.print;

import android.content.Context;
import android.text.TextUtils;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.feature.export.job.pdf.view.SizedTable;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PdfLog implements PdfPrintable {

    private final PdfExportCache cache;
    private final PdfCellFactory cellFactory;

    PdfLog(PdfExportCache cache, PdfCellFactory cellFactory) {
        this.cache = cache;
        this.cellFactory = cellFactory;
    }

    @Override
    public void print(List<Entry> entriesOfDay) throws Exception {
        if (entriesOfDay.isEmpty()) {
            addRows(Arrays.asList(
                Collections.singletonList(cellFactory.getDayCell()),
                cellFactory.getEmptyRow()),
                false
            );
        } else {
            for (int entryIndex = 0; entryIndex < entriesOfDay.size(); entryIndex++) {
                addRows(getRowsForEntryIndex(entriesOfDay, entryIndex), true);
            }
        }
        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + PdfPage.MARGIN);
    }

    private void addRows(List<List<Cell>> rows, boolean appendHeaderIfNeeded) throws Exception {
        SizedTable table = new SizedTable();

        float rowHeight = 0f;
        for (List<Cell> row : rows) {
            rowHeight += row.get(row.size() - 1).getHeight();
        }
        if (cache.getPage().getPosition().getY() + rowHeight > cache.getPage().getEndPoint().getY()) {
            cache.setPage(new PdfPage(cache));
            List<List<Cell>> rowsWithHeader = new ArrayList<>();
            if (appendHeaderIfNeeded) {
                rowsWithHeader.add(Collections.singletonList(cellFactory.getDayCell()));
            }
            rowsWithHeader.addAll(rows);
            table.setData(rowsWithHeader);
        } else {
            table.setData(rows);
        }

        table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
        table.drawOn(cache.getPage());

        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + table.getHeight());
    }

    private List<List<Cell>> getRowsForEntryIndex(List<Entry> entriesOfDay, int entryIndex) {
        List<List<Cell>> rows = new ArrayList<>();

        PdfExportConfig config = cache.getConfig();
        Context context = config.getContext();

        if (entryIndex == 0) {
            rows.add(Collections.singletonList(cellFactory.getDayCell()));
        }
        Entry entry = entriesOfDay.get(entryIndex);
        boolean isFirstMeasurementOfEntry = true;
        int backgroundColor = entryIndex % 2 == 0 ? cache.getColorDivider() : Color.white;
        String time = entry.getDate().toString("HH:mm");

        List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry, cache.getConfig().getCategories());
        for (Measurement measurement : measurements) {
            Category category = measurement.getCategory();
            int textColor = Color.black;
            if (category == Category.BLOODSUGAR && config.highlightLimits()) {
                BloodSugar bloodSugar = (BloodSugar) measurement;
                float value = bloodSugar.getMgDl();
                if (value > PreferenceStore.getInstance().getLimitHyperglycemia()) {
                    textColor = cache.getColorHyperglycemia();
                } else if (value < PreferenceStore.getInstance().getLimitHypoglycemia()) {
                    textColor = cache.getColorHypoglycemia();
                }
            }

            String measurementText = measurement.print(context);

            if (category == Category.MEAL && config.exportFood()) {
                List<String> foodOfDay = new ArrayList<>();
                Meal meal = (Meal) MeasurementDao.getInstance(Meal.class).getMeasurement(entry);
                if (meal != null) {
                    for (FoodEaten foodEaten : FoodEatenDao.getInstance().getAll(meal)) {
                        String foodNote = foodEaten.print();
                        if (foodNote != null) {
                            foodOfDay.add(foodNote);
                        }
                    }
                }
                if (!foodOfDay.isEmpty()) {
                    String foodText = TextUtils.join(", ", foodOfDay);
                    measurementText = String.format("%s\n%s", measurementText, foodText);
                }
            }

            List<Cell> row = cellFactory.getLogRow(
                time,
                context.getString(category.getStringAcronymResId()),
                measurementText,
                backgroundColor,
                textColor
            );
            rows.add(row);
            isFirstMeasurementOfEntry= false;
        }

        if (config.exportTags()) {
            List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
            if (!entryTags.isEmpty()) {
                List<String> tagNames = new ArrayList<>();
                for (EntryTag entryTag : entryTags) {
                    Tag tag = entryTag.getTag();
                    if (tag != null) {
                        String tagName = tag.getName();
                        if (!StringUtils.isBlank(tagName)) {
                            tagNames.add(tagName);
                        }
                    }
                }
                List<Cell> row = cellFactory.getLogRow(
                    isFirstMeasurementOfEntry ? time : null,
                    context.getString(R.string.tags),
                    TextUtils.join(", ", tagNames),
                    backgroundColor
                );
                rows.add(row);
                isFirstMeasurementOfEntry = false;
            }
        }

        if (config.exportNotes() && !StringUtils.isBlank(entry.getNote())) {
            List<Cell> row = cellFactory.getLogRow(
                isFirstMeasurementOfEntry ? time : null,
                context.getString(R.string.note),
                entry.getNote(),
                backgroundColor
            );
            rows.add(row);
        }
        return rows;
    }
}
