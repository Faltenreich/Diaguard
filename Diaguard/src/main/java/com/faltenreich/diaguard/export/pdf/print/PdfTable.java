package com.faltenreich.diaguard.export.pdf.print;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.faltenreich.diaguard.R;
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
import com.faltenreich.diaguard.export.pdf.view.DayCell;
import com.faltenreich.diaguard.export.pdf.view.HourCell;
import com.faltenreich.diaguard.export.pdf.view.LabelCell;
import com.faltenreich.diaguard.export.pdf.view.MeasurementCell;
import com.faltenreich.diaguard.export.pdf.view.NoteCell;
import com.faltenreich.diaguard.export.pdf.view.Cell;
import com.faltenreich.diaguard.export.pdf.view.SizedTable;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.StringUtils;
import com.pdfjet.Border;
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

        List<List<com.pdfjet.Cell>> data = new ArrayList<>();

        List<com.pdfjet.Cell> cells = new ArrayList<>();
        cells.add(new DayCell(cache.getFontBold(), cache.getDateTime()));
        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += PdfTable.HOURS_TO_SKIP) {
            cells.add(new HourCell(cache.getFontNormal(), hour, cellWidth));
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

                    ArrayList<com.pdfjet.Cell> noteCells = new ArrayList<>();

                    com.pdfjet.Cell timeCell = new Cell(cache.getFontNormal());
                    timeCell.setWidth(LABEL_WIDTH);
                    timeCell.setText(Helper.getTimeFormat().print(note.getDateTime()));
                    timeCell.setFgColor(Color.gray);
                    if (isFirst) {
                        timeCell.setBorder(Border.TOP, true);
                    }
                    if (isLast) {
                        timeCell.setBorder(Border.BOTTOM, true);
                    }
                    noteCells.add(timeCell);

                    NoteCell noteCell = new NoteCell(cache.getFontNormal());
                    noteCell.setText(note.getNote());
                    noteCell.setFgColor(Color.gray);
                    noteCell.setWidth(width - PdfTable.LABEL_WIDTH);
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

    private List<com.pdfjet.Cell> createMeasurementRows(PdfExportCache cache, ListItemCategoryValue[] items, float cellWidth, int valueIndex, String label, int backgroundColor) {
        List<com.pdfjet.Cell> cells = new ArrayList<>();

        cells.add(new LabelCell(cache.getFontNormal(), label, backgroundColor));

        for (ListItemCategoryValue item : items) {
            cells.add(new MeasurementCell(cache, item, valueIndex, backgroundColor, cellWidth));
        }
        return cells;
    }
}
