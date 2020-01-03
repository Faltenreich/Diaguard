package com.faltenreich.diaguard.export.pdf.print;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.export.pdf.view.CellBuilder;
import com.faltenreich.diaguard.export.pdf.view.CellFactory;
import com.faltenreich.diaguard.export.pdf.view.MultilineCell;
import com.faltenreich.diaguard.export.pdf.view.SizedTable;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.StringUtils;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Point;

import java.util.ArrayList;
import java.util.List;

public class PdfLog implements PdfPrintable {

    private static final String TAG = PdfLog.class.getSimpleName();
    private static final float TIME_WIDTH = 72;

    private PdfExportCache cache;
    private SizedTable table;

    PdfLog(PdfExportCache cache) {
        this.cache = cache;
        this.table = new SizedTable();
        init();
    }

    @Override
    public float getHeight() {
        float height = table.getHeight();
        return height > 0 ? height + PdfPage.MARGIN : 0f;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        table.setLocation(position.getX(), position.getY());
        table.drawOn(page);
    }

    private void init() {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContext();

        List<List<Cell>> data = new ArrayList<>();
        List<Cell> headerRow = new ArrayList<>();
        Cell headerCell = new CellBuilder(new Cell(cache.getFontBold()))
            .setWidth(getLabelWidth())
            .setText(DateTimeUtils.toWeekDayAndDate(cache.getDateTime()))
            .build();
        headerRow.add(headerCell);
        data.add(headerRow);

        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(cache.getDateTime());
        for (Entry entry : entries) {
            List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry, cache.getConfig().getCategories());
            entry.setMeasurementCache(measurements);
        }

        int rowIndex = 0;
        for (Entry entry : entries) {
            int backgroundColor = rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white;
            int oldSize = data.size();
            String time = entry.getDate().toString("HH:mm");

            for (Measurement measurement : entry.getMeasurementCache()) {
                Measurement.Category category = measurement.getCategory();
                int textColor = Color.black;
                if (category == Measurement.Category.BLOODSUGAR && config.isHighlightLimits()) {
                    BloodSugar bloodSugar = (BloodSugar) measurement;
                    float value = bloodSugar.getMgDl();
                    if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                        textColor = cache.getColorHyperglycemia();
                    } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                        textColor = cache.getColorHypoglycemia();
                    }
                }

                String measurementText = measurement.print();

                if (category == Measurement.Category.MEAL && config.isExportFood()) {
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

                data.add(getRow(
                    cache,
                    data.size() == oldSize ? time : null,
                    context.getString(category.getStringAcronymResId()),
                    measurementText,
                    backgroundColor,
                    textColor
                ));
            }

            if (config.isExportTags()) {
                List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                if (!entryTags.isEmpty()) {
                    List<String> tagNames = new ArrayList<>();
                    for (EntryTag entryTag : entryTags) {
                        String tagName = entryTag.getTag().getName();
                        if (!StringUtils.isBlank(tagName)) {
                            tagNames.add(entryTag.getTag().getName());
                        }
                    }
                    data.add(getRow(
                        cache,
                        data.size() == oldSize ? time : null,
                        context.getString(R.string.tags),
                        TextUtils.join(", ", tagNames),
                        backgroundColor
                    ));
                }
            }

            if (config.isExportNotes()) {
                if (!StringUtils.isBlank(entry.getNote())) {
                    data.add(getRow(
                        cache,
                        data.size() == oldSize ? time : null,
                        context.getString(R.string.note),
                        entry.getNote(),
                        backgroundColor
                    ));
                }
            }

            rowIndex++;
        }

        boolean hasData = data.size() > 1;
        if (!hasData) {
            data.add(CellFactory.createEmptyRow(cache));
        }

        try {
            table.setData(data);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
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
