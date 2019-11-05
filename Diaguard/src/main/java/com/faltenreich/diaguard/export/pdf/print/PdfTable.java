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
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.export.pdf.meta.PdfNote;
import com.faltenreich.diaguard.export.pdf.view.CellBuilder;
import com.faltenreich.diaguard.export.pdf.view.MultilineCell;
import com.faltenreich.diaguard.export.pdf.view.SizedTable;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.StringUtils;
import com.pdfjet.Align;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Point;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PdfTable implements PdfPrintable {

    private static final String TAG = PdfTable.class.getSimpleName();
    private static final float PADDING_PARAGRAPH = 20;
    public static final float LABEL_WIDTH = 120;
    public static final int HOURS_TO_SKIP = 2;

    private SizedTable table;
    private float width;

    public PdfTable(PdfExportCache cache, float width) {
        this.table = new SizedTable();
        this.width = width;
        init(cache);
    }

    @Override
    public float getHeight() {
        float height = table.getHeight();
        return height > 0 ? height + PADDING_PARAGRAPH : 0f;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        table.setLocation(position.getX(), position.getY());
        table.drawOn(page);
    }

    private void init(PdfExportCache cache) {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContextReference().get();
        float cellWidth = (width - LABEL_WIDTH) / (DateTimeConstants.HOURS_PER_DAY / 2f);

        List<List<Cell>> data = new ArrayList<>();

        List<Cell> cells = new ArrayList<>();
        Cell headerCell = new CellBuilder(new Cell(cache.getFontBold()))
            .setWidth(LABEL_WIDTH)
            .setText(cache.getDateTime())
            .build();
        cells.add(headerCell);
        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += PdfTable.HOURS_TO_SKIP) {
            Cell hourCell = new CellBuilder(new Cell(cache.getFontNormal()))
                .setWidth(cellWidth)
                .setText(Integer.toString(hour))
                .setForegroundColor(Color.gray)
                .setTextAlignment(Align.CENTER)
                .build();
            cells.add(hourCell);
        }
        data.add(cells);

        LinkedHashMap<Measurement.Category, ListItemCategoryValue[]> values = EntryDao.getInstance().getAverageDataTable(cache.getDateTime(), config.getCategories(), HOURS_TO_SKIP);
        int row = 0;
        for (Measurement.Category category : values.keySet()) {
            ListItemCategoryValue[] items = values.get(category);
            if (items != null) {
                String label = category.toLocalizedString(context);
                int backgroundColor = row % 2 == 0 ? cache.getColorDivider() : Color.white;
                switch (category) {
                    case INSULIN:
                        if (config.isSplitInsulin()) {
                            data.add(createMeasurementRows(cache, items, cellWidth, 0, label + " " + context.getString(R.string.bolus), backgroundColor));
                            data.add(createMeasurementRows(cache, items, cellWidth, 1, label + " " + context.getString(R.string.correction), backgroundColor));
                            data.add(createMeasurementRows(cache, items, cellWidth, 2, label + " " + context.getString(R.string.basal), backgroundColor));
                        } else {
                            data.add(createMeasurementRows(cache, items, cellWidth, -1, label, backgroundColor));
                        }
                        break;
                    case PRESSURE:
                        data.add(createMeasurementRows(cache, items, cellWidth, 0, label + " " + context.getString(R.string.systolic_acronym), backgroundColor));
                        data.add(createMeasurementRows(cache, items, cellWidth, 1, label + " " + context.getString(R.string.diastolic_acronym), backgroundColor));
                        break;
                    default:
                        data.add(createMeasurementRows(cache, items, cellWidth, 0, label, backgroundColor));
                        break;
                }
                row++;
            }
        }

        if (config.isExportNotes() || config.isExportTags() || config.isExportFood()) {
            List<PdfNote> notes = new ArrayList<>();
            for (Entry entry : EntryDao.getInstance().getEntriesOfDay(cache.getDateTime())) {
                List<String> entryNotesAndTagsOfDay = new ArrayList<>();
                List<String> foodOfDay = new ArrayList<>();
                if (config.isExportNotes() && !StringUtils.isBlank(entry.getNote())) {
                    entryNotesAndTagsOfDay.add(entry.getNote());
                }
                if (config.isExportTags()) {
                    List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                    for (EntryTag entryTag : entryTags) {
                        entryNotesAndTagsOfDay.add(entryTag.getTag().getName());
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
                boolean hasAny = hasEntryNotesAndTags || hasFood;
                if (hasAny) {
                    boolean hasBoth = hasEntryNotesAndTags && hasFood;
                    String notesOfDay = hasBoth ?
                        // Break line for succeeding food
                        TextUtils.join("\n", new String[] { getNotesAsString(entryNotesAndTagsOfDay), getNotesAsString(foodOfDay) }) :
                        hasEntryNotesAndTags ?
                            getNotesAsString(entryNotesAndTagsOfDay) :
                            getNotesAsString(foodOfDay);
                    notes.add(new PdfNote(entry.getDate(), notesOfDay));
                }
            }
            if (notes.size() > 0) {
                for (PdfNote note : notes) {
                    boolean isFirst = notes.indexOf(note) == 0;
                    boolean isLast = notes.indexOf(note) == notes.size() - 1;

                    ArrayList<Cell> noteCells = new ArrayList<>();

                    Cell timeCell = new CellBuilder(new Cell(cache.getFontNormal()))
                        .setWidth(LABEL_WIDTH)
                        .setText(Helper.getTimeFormat().print(note.getDateTime()))
                        .setForegroundColor(Color.gray)
                        .build();
                    if (isFirst) {
                        timeCell.setBorder(Border.TOP, true);
                    }
                    if (isLast) {
                        timeCell.setBorder(Border.BOTTOM, true);
                    }
                    noteCells.add(timeCell);

                    Cell noteCell = new CellBuilder(new MultilineCell(cache.getFontNormal()))
                        .setWidth(width - PdfTable.LABEL_WIDTH)
                        .setText(note.getNote())
                        .setForegroundColor(Color.gray)
                        .build();
                    if (isFirst) {
                        noteCell.setBorder(Border.TOP, true);
                    }
                    if (isLast) {
                        noteCell.setBorder(Border.BOTTOM, true);
                    }
                    noteCells.add(noteCell);

                    data.add(noteCells);
                }
            }
        }

        try {
            table.setData(data);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    private String getNotesAsString(List<String> notes) {
        return TextUtils.join(", ", notes);
    }

    private List<Cell> createMeasurementRows(PdfExportCache cache, ListItemCategoryValue[] items, float cellWidth, int valueIndex, String label, int backgroundColor) {
        List<Cell> cells = new ArrayList<>();

        Cell labelCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(PdfTable.LABEL_WIDTH)
            .setText(label)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        cells.add(labelCell);

        for (ListItemCategoryValue item : items) {
            Measurement.Category category = item.getCategory();
            float value = 0;
            switch (valueIndex) {
                case -1:
                    value = item.getValueTotal();
                    break;
                case 0:
                    value = item.getValueOne();
                    break;
                case 1:
                    value = item.getValueTwo();
                    break;
                case 2:
                    value = item.getValueThree();
                    break;
            }
            int textColor = Color.black;
            if (category == Measurement.Category.BLOODSUGAR && cache.getConfig().isHighlightLimits()) {
                if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                    textColor = cache.getColorHyperglycemia();
                } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                    textColor = cache.getColorHypoglycemia();
                }
            }
            float customValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, value);
            String text = customValue > 0 ? Helper.parseFloat(customValue) : "";

            Cell measurementCell = new CellBuilder(new Cell(cache.getFontNormal()))
                .setWidth(cellWidth)
                .setText(text)
                .setTextAlignment(Align.CENTER)
                .setBackgroundColor(backgroundColor)
                .setForegroundColor(textColor)
                .build();
            cells.add(measurementCell);
        }
        return cells;
    }
}
