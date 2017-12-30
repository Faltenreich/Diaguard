package com.faltenreich.diaguard.util.export;

import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValue;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;
import com.pdfjet.Align;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.PDF;
import com.pdfjet.Table;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Faltenreich on 19.10.2015.
 */
public class PdfTable extends Table {

    private static final String TAG = PdfTable.class.getSimpleName();

    private static final int ALTERNATING_ROW_COLOR = ContextCompat.getColor(DiaguardApplication.getContext(), R.color.light);
    private static final float LABEL_WIDTH = 120;
    private static final int HOURS_TO_SKIP = 2;

    private PDF pdf;
    private PdfPage page;
    private DateTime day;
    private Measurement.Category[] categories;
    private boolean exportNotes;

    private Font fontNormal;
    private Font fontBold;

    private int rows;

    PdfTable(PDF pdf, PdfPage page, DateTime day, Measurement.Category[] categories, boolean exportNotes) {
        super();
        this.pdf = pdf;
        this.page = page;
        this.day = day;
        this.categories = categories;
        this.exportNotes = exportNotes;
        init();
    }

    private void init() {
        try {
            fontNormal = new Font(pdf, CoreFont.HELVETICA);
            fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);
            setData(getData());
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    private float getCellWidth() {
        return (page.getWidth() - LABEL_WIDTH) / (DateTimeConstants.HOURS_PER_DAY / 2);
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
        List<List<Cell>> data = new ArrayList<>();

        data.add(getRowForHeader());

        LinkedHashMap<Measurement.Category, ListItemCategoryValue[]> values = EntryDao.getInstance().getAverageDataTable(day, categories, HOURS_TO_SKIP);
        int row = 0;
        for (Measurement.Category category : values.keySet()) {
            ListItemCategoryValue[] items = values.get(category);
            String label = category.toLocalizedString();
            int backgroundColor = row % 2 == 0 ? ALTERNATING_ROW_COLOR : Color.white;
            if (category == Measurement.Category.PRESSURE) {
                data.add(getRowForValues(items, 0, label + " " + DiaguardApplication.getContext().getString(R.string.systolic_acronym), backgroundColor));
                data.add(getRowForValues(items, 1, label + " " + DiaguardApplication.getContext().getString(R.string.diastolic_acronym), backgroundColor));
            } else {
                data.add(getRowForValues(items, 0, label, backgroundColor));
            }
            row++;
        }

        if (exportNotes) {
            List<Entry> entriesWithNotes = EntryDao.getInstance().getAllWithNotes(day);
            if (entriesWithNotes.size() > 0) {
                for (Entry entry : entriesWithNotes) {
                    data.add(getRowForNote(entry,
                            entriesWithNotes.indexOf(entry) == 0,
                            entriesWithNotes.indexOf(entry) == entriesWithNotes.size() - 1));
                }
            }
        }

        rows = data.size();

        return data;
    }

    private List<Cell> getRowForHeader() {
        List<Cell> cells = new ArrayList<>();

        String weekDay = DateTimeFormat.forPattern("E").print(day);
        String date = String.format("%s %s", weekDay, DateTimeFormat.forPattern("dd.MM").print(day));
        Cell cell = new Cell(fontBold, date);
        cell.setWidth(LABEL_WIDTH);
        cell.setNoBorders();
        cells.add(cell);

        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += HOURS_TO_SKIP) {
            cell = new Cell(fontNormal, Integer.toString(hour));
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

        Cell cell = new Cell(fontNormal, label);
        cell.setBgColor(backgroundColor);
        cell.setFgColor(Color.gray);
        cell.setWidth(LABEL_WIDTH);
        cell.setNoBorders();
        cells.add(cell);

        for (ListItemCategoryValue item : items) {
            Measurement.Category category = item.getCategory();
            float value = valueIndex == 0 ? item.getValueOne() : item.getValueTwo();
            int textColor = Color.black;
            if (category == Measurement.Category.BLOODSUGAR && PreferenceHelper.getInstance().limitsAreHighlighted()) {
                if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                    textColor = ContextCompat.getColor(DiaguardApplication.getContext(), R.color.red);
                } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                    textColor = ContextCompat.getColor(DiaguardApplication.getContext(), R.color.blue);
                }
            }
            float customValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, value);
            String text = customValue > 0 ? Helper.parseFloat(customValue) : "";
            cell = new Cell(fontNormal, text);
            cell.setBgColor(backgroundColor);
            cell.setFgColor(textColor);
            cell.setWidth(getCellWidth());
            cell.setTextAlignment(Align.CENTER);
            cell.setNoBorders();
            cells.add(cell);
        }
        return cells;
    }

    private List<Cell> getRowForNote(Entry entry, boolean isFirst, boolean isLast) {
        ArrayList<Cell> cells = new ArrayList<>();

        Cell cell = new Cell(fontNormal, Helper.getTimeFormat().print(entry.getDate()));
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

        PdfMultilineCell multilineCell = new PdfMultilineCell(fontNormal, entry.getNote(), 55);
        multilineCell.setFgColor(Color.gray);
        multilineCell.setWidth(page.getWidth() - LABEL_WIDTH);
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
