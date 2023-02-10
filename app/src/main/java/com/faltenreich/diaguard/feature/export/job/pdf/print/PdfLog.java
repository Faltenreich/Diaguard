package com.faltenreich.diaguard.feature.export.job.pdf.print;

import android.content.Context;
import android.text.TextUtils;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.feature.export.job.pdf.view.CellBuilder;
import com.faltenreich.diaguard.feature.export.job.pdf.view.CellFactory;
import com.faltenreich.diaguard.feature.export.job.pdf.view.MultilineCell;
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
    private static final float TIME_WIDTH = 72;

    private final PdfExportCache cache;
    private final CellFactory cellFactory;
    private final List<Entry> entriesOfDay;
    private final List<List<List<Cell>>> data;

    PdfLog(PdfExportCache cache, List<Entry> entriesOfDay) {
        this.cache = cache;
        this.cellFactory = new CellFactory(cache);
        this.entriesOfDay = entriesOfDay;
        this.data = new ArrayList<>();
        init();
    }

    @Override
    public void drawOn(PdfPage page) throws Exception {
        SizedTable table = new SizedTable();

        Cell headerCell = cellFactory.getDayCell();
        float rowHeight = headerCell.getHeight();
        table.setData(Collections.singletonList(Collections.singletonList(headerCell)));
        if (page.getPosition().getY() + rowHeight > page.getEndPoint().getY()) {
            page = new PdfPage(cache);
        }
        table.setLocation(page.getPosition().getX(), page.getPosition().getY());
        table.drawOn(page);
        page.getPosition().setY(page.getPosition().getY() + rowHeight);

        for (List<List<Cell>> entry : data) {
            rowHeight = 0f;
            for (List<Cell> row : entry) {
                rowHeight += row.get(COLUMN_INDEX_NOTE).getHeight();
            }
            if (page.getPosition().getY() + rowHeight > page.getEndPoint().getY()) {
                page = new PdfPage(cache);
                rowHeight += headerCell.getHeight();
                entry.add(0, Collections.singletonList(headerCell));
            }
            table.setData(entry);
            table.setLocation(page.getPosition().getX(), page.getPosition().getY());
            table.drawOn(page);
            page.getPosition().setY(page.getPosition().getY() + rowHeight);
        }

        if (data.isEmpty()) {
            List<Cell> row = cellFactory.getEmptyCells();
            rowHeight = row.get(0).getHeight();
            table.setData(Collections.singletonList(row));
            if (page.getPosition().getY() + rowHeight > page.getEndPoint().getY()) {
                page = new PdfPage(cache);
            }
            table.setLocation(page.getPosition().getX(), page.getPosition().getY());
            table.drawOn(page);
            page.getPosition().setY(page.getPosition().getY() + rowHeight);
        }

        page.getPosition().setY(page.getPosition().getY() + PdfPage.MARGIN);

        cache.setPage(page);
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
            int oldSize = data.size();
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

                rows.add(getRow(
                    cache,
                    time,
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
                    rows.add(getRow(
                        cache,
                        rows.isEmpty() ? time : null,
                        context.getString(R.string.tags),
                        TextUtils.join(", ", tagNames),
                        backgroundColor
                    ));
                }
            }

            if (config.exportNotes() && !StringUtils.isBlank(entry.getNote())) {
                rows.add(getRow(
                    cache,
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

    private List<Cell> getRow(PdfExportCache cache, String title, String subtitle, String description, int backgroundColor, int foregroundColor) {
        List<Cell> entryRow = new ArrayList<>();
        float width = cache.getPage().getWidth();

        Cell titleCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(PdfLog.TIME_WIDTH)
            .setText(title)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        entryRow.add(titleCell);

        Cell subtitleCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(getLabelWidth())
            .setText(subtitle)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        entryRow.add(subtitleCell);

        Cell descriptionCell = new CellBuilder(new MultilineCell(cache.getFontNormal()))
            .setWidth(width - titleCell.getWidth() - subtitleCell.getWidth())
            .setText(description)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(foregroundColor)
            .build();
        entryRow.add(descriptionCell);

        return entryRow;
    }

    private List<Cell> getRow(PdfExportCache cache, String title, String subtitle, String description, int backgroundColor) {
        return getRow(cache, title, subtitle, description, backgroundColor, Color.black);
    }
}
