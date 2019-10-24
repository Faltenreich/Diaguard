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
import com.faltenreich.diaguard.export.pdf.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.PdfExportConfig;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.StringUtils;
import com.pdfjet.Align;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Table;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PdfTable extends Table {

    private static final String TAG = PdfTable.class.getSimpleName();
    private static final float LABEL_WIDTH = 120;
    private static final int HOURS_TO_SKIP = 2;

    private PdfExportCache cache;
    private float width;

    private int rows;

    public PdfTable(PdfExportCache cache, float width) {
        super();
        this.cache = cache;
        this.width = width;
        init();
    }

    private Context getContext() {
        return cache.getConfig().getContextReference().get();
    }

    private void init() {
        try {
            setData(getData());
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    private float getCellWidth() {
        return (width - LABEL_WIDTH) / (DateTimeConstants.HOURS_PER_DAY / 2f);
    }

    public float getHeight() {
        float height = 0;
        for (int row = 0; row < rows; row++) {
            try {
                height += getRowAtIndex(row).get(0).getHeight();
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
        return height;
    }

    private List<List<Cell>> getData() {
        PdfExportConfig config = cache.getConfig();
        DateTime day = cache.getDateTime();
        List<List<Cell>> data = new ArrayList<>();
        data.add(getRowForHeader());

        LinkedHashMap<Measurement.Category, ListItemCategoryValue[]> values = EntryDao.getInstance().getAverageDataTable(day, config.getCategories(), HOURS_TO_SKIP);
        int row = 0;
        for (Measurement.Category category : values.keySet()) {
            ListItemCategoryValue[] items = values.get(category);
            if (items != null) {
                String label = category.toLocalizedString(getContext());
                int backgroundColor = row % 2 == 0 ? cache.getColorDivider() : Color.white;
                switch (category) {
                    case INSULIN:
                        if (config.isSplitInsulin()) {
                            data.add(getRowForValues(items, 0, label + " " + getContext().getString(R.string.bolus), backgroundColor));
                            data.add(getRowForValues(items, 1, label + " " + getContext().getString(R.string.correction), backgroundColor));
                            data.add(getRowForValues(items, 2, label + " " + getContext().getString(R.string.basal), backgroundColor));
                        } else {
                            data.add(getRowForValues(items, -1, label, backgroundColor));
                        }
                        break;
                    case PRESSURE:
                        data.add(getRowForValues(items, 0, label + " " + getContext().getString(R.string.systolic_acronym), backgroundColor));
                        data.add(getRowForValues(items, 1, label + " " + getContext().getString(R.string.diastolic_acronym), backgroundColor));
                        break;
                    default:
                        data.add(getRowForValues(items, 0, label, backgroundColor));
                        break;
                }
                row++;
            }
        }

        if (config.isExportNotes() || config.isExportTags() || config.isExportFood()) {
            List<PdfNote> notes = new ArrayList<>();
            for (Entry entry : EntryDao.getInstance().getEntriesOfDay(day)) {
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
                    data.add(getRowForNote(note, isFirst, isLast));
                }
            }
        }

        rows = data.size();
        return data;
    }

    private String getNotesAsString(List<String> notes) {
        return TextUtils.join(", ", notes);
    }

    private List<Cell> getRowForHeader() {
        List<Cell> cells = new ArrayList<>();

        DateTime day = cache.getDateTime();
        String weekDay = DateTimeFormat.forPattern("E").print(day);
        String date = String.format("%s %s", weekDay, DateTimeFormat.forPattern("dd.MM").print(day));
        Cell cell = new Cell(cache.getFontBold(), date);
        cell.setWidth(LABEL_WIDTH);
        cell.setNoBorders();
        cells.add(cell);

        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += HOURS_TO_SKIP) {
            cell = new Cell(cache.getFontNormal(), Integer.toString(hour));
            cell.setWidth(getCellWidth());
            cell.setFgColor(Color.gray);
            cell.setTextAlignment(Align.CENTER);
            cell.setNoBorders();
            cells.add(cell);
        }

        return cells;
    }

    private List<Cell> getRowForValues(ListItemCategoryValue[] items, int valueIndex, String label, int backgroundColor) {
        List<Cell> cells = new ArrayList<>();

        Cell cell = new Cell(cache.getFontNormal(), label);
        cell.setBgColor(backgroundColor);
        cell.setFgColor(Color.gray);
        cell.setWidth(LABEL_WIDTH);
        cell.setNoBorders();
        cells.add(cell);

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
            if (category == Measurement.Category.BLOODSUGAR && PreferenceHelper.getInstance().limitsAreHighlighted()) {
                if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                    textColor = cache.getColorHyperglycemia();
                } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                    textColor = cache.getColorHypoglycemia();
                }
            }
            float customValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, value);
            String text = customValue > 0 ? Helper.parseFloat(customValue) : "";
            cell = new Cell(cache.getFontNormal(), text);
            cell.setBgColor(backgroundColor);
            cell.setFgColor(textColor);
            cell.setWidth(getCellWidth());
            cell.setTextAlignment(Align.CENTER);
            cell.setNoBorders();
            cells.add(cell);
        }
        return cells;
    }

    private List<Cell> getRowForNote(PdfNote note, boolean isFirst, boolean isLast) {
        ArrayList<Cell> cells = new ArrayList<>();

        Cell cell = new Cell(cache.getFontNormal(), Helper.getTimeFormat().print(note.getDateTime()));
        cell.setFgColor(Color.gray);
        cell.setWidth(LABEL_WIDTH);
        cell.setNoBorders();

        if (isFirst) {
            cell.setBorder(Border.TOP, true);
        }
        if (isLast) {
            cell.setBorder(Border.BOTTOM, true);
        }

        cells.add(cell);

        PdfMultilineCell multilineCell = new PdfMultilineCell(cache.getFontNormal(), note.getNote(), 55);
        multilineCell.setFgColor(Color.gray);
        multilineCell.setWidth(width - LABEL_WIDTH);
        multilineCell.setNoBorders();

        if (isFirst) {
            multilineCell.setBorder(Border.TOP, true);
        }
        if (isLast) {
            multilineCell.setBorder(Border.BOTTOM, true);
        }

        cells.add(multilineCell);
        return cells;
    }
}
