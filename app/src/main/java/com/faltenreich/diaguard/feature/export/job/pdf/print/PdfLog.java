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
import java.util.Collections;
import java.util.List;

public class PdfLog implements PdfPrintable {

    private static final int COLUMN_INDEX_NOTE = 2;

    private final PdfExportCache cache;
    private final PdfCellFactory cellFactory;
    private final List<Entry> entriesOfDay;
    private final List<List<List<Cell>>> data;

    PdfLog(PdfExportCache cache, List<Entry> entriesOfDay) {
        this.cache = cache;
        this.cellFactory = new PdfCellFactory(cache);
        this.entriesOfDay = entriesOfDay;
        this.data = new ArrayList<>();
        init();
    }

    @Override
    public void print() throws Exception {
        SizedTable table = new SizedTable();

        Cell headerCell = cellFactory.getDayCell();
        float rowHeight = headerCell.getHeight();
        table.setData(Collections.singletonList(Collections.singletonList(headerCell)));
        if (cache.getPage().getPosition().getY() + rowHeight > cache.getPage().getEndPoint().getY()) {
            cache.setPage(new PdfPage(cache));
        }
        table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
        table.drawOn(cache.getPage());
        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + rowHeight);

        for (List<List<Cell>> entry : data) {
            rowHeight = 0f;
            for (List<Cell> row : entry) {
                rowHeight += row.get(COLUMN_INDEX_NOTE).getHeight();
            }
            if (cache.getPage().getPosition().getY() + rowHeight > cache.getPage().getEndPoint().getY()) {
                cache.setPage(new PdfPage(cache));
                rowHeight += headerCell.getHeight();
                entry.add(0, Collections.singletonList(headerCell));
            }
            table.setData(entry);
            table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
            table.drawOn(cache.getPage());
            cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + rowHeight);
        }

        if (data.isEmpty()) {
            List<Cell> row = cellFactory.getEmptyRow();
            rowHeight = row.get(0).getHeight();
            table.setData(Collections.singletonList(row));
            if (cache.getPage().getPosition().getY() + rowHeight > cache.getPage().getEndPoint().getY()) {
                cache.setPage(new PdfPage(cache));
            }
            table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
            table.drawOn(cache.getPage());
            cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + rowHeight);
        }

        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + PdfPage.MARGIN);
    }

    private void init() {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContext();

        for (Entry entry : entriesOfDay) {
            List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry, cache.getConfig().getCategories());
            entry.setMeasurementCache(measurements);
        }

        int rowIndex = 0;
        for (Entry entry : entriesOfDay) {
            List<List<Cell>> rows = new ArrayList<>();
            int backgroundColor = rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white;
            String time = entry.getDate().toString("HH:mm");

            for (Measurement measurement : entry.getMeasurementCache()) {
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

                rows.add(cellFactory.getLogRow(
                    rows.isEmpty() ? time : null,
                    context.getString(category.getStringAcronymResId()),
                    measurementText,
                    backgroundColor,
                    textColor
                ));
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
                    rows.add(cellFactory.getLogRow(
                        rows.isEmpty() ? time : null,
                        context.getString(R.string.tags),
                        TextUtils.join(", ", tagNames),
                        backgroundColor
                    ));
                }
            }

            if (config.exportNotes() && !StringUtils.isBlank(entry.getNote())) {
                rows.add(cellFactory.getLogRow(
                    rows.isEmpty() ? time : null,
                    context.getString(R.string.note),
                    entry.getNote(),
                    backgroundColor
                ));
            }

            rowIndex++;
            data.add(rows);
        }
    }
}
