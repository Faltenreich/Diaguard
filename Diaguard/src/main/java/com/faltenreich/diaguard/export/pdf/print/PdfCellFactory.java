package com.faltenreich.diaguard.export.pdf.print;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.view.MultilineCellPdfView;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.util.Helper;
import com.pdfjet.Align;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

class PdfCellFactory {

    static List<Cell> createHeaderRows(PdfExportCache cache, float cellWidth) {
        List<Cell> cells = new ArrayList<>();

        DateTime day = cache.getDateTime();
        String weekDay = DateTimeFormat.forPattern("E").print(day);
        String date = String.format("%s %s", weekDay, DateTimeFormat.forPattern("dd.MM").print(day));
        Cell cell = new Cell(cache.getFontBold(), date);
        cell.setWidth(PdfTable.LABEL_WIDTH);
        cell.setNoBorders();
        cells.add(cell);

        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += PdfTable.HOURS_TO_SKIP) {
            cell = new Cell(cache.getFontNormal(), Integer.toString(hour));
            cell.setWidth(cellWidth);
            cell.setFgColor(Color.gray);
            cell.setTextAlignment(Align.CENTER);
            cell.setNoBorders();
            cells.add(cell);
        }

        return cells;
    }

    static List<Cell> createMeasurementRows(PdfExportCache cache, ListItemCategoryValue[] items, float cellWidth, int valueIndex, String label, int backgroundColor) {
        List<Cell> cells = new ArrayList<>();

        Cell cell = new Cell(cache.getFontNormal(), label);
        cell.setBgColor(backgroundColor);
        cell.setFgColor(Color.gray);
        cell.setWidth(PdfTable.LABEL_WIDTH);
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
            cell.setWidth(cellWidth);
            cell.setTextAlignment(Align.CENTER);
            cell.setNoBorders();
            cells.add(cell);
        }
        return cells;
    }

    static List<Cell> createNoteRow(PdfExportCache cache, PdfNote note, float rowWidth, boolean isFirst, boolean isLast) {
        ArrayList<Cell> cells = new ArrayList<>();

        Cell cell = new Cell(cache.getFontNormal(), Helper.getTimeFormat().print(note.getDateTime()));
        cell.setFgColor(Color.gray);
        cell.setWidth(PdfTable.LABEL_WIDTH);
        cell.setNoBorders();

        if (isFirst) {
            cell.setBorder(Border.TOP, true);
        }
        if (isLast) {
            cell.setBorder(Border.BOTTOM, true);
        }

        cells.add(cell);

        MultilineCellPdfView multilineCell = new MultilineCellPdfView(cache.getFontNormal(), note.getNote(), 55);
        multilineCell.setFgColor(Color.gray);
        multilineCell.setWidth(rowWidth - PdfTable.LABEL_WIDTH);
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
