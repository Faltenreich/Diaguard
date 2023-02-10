package com.faltenreich.diaguard.feature.export.job.pdf.print;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNote;
import com.faltenreich.diaguard.feature.export.job.pdf.view.CellBuilder;
import com.faltenreich.diaguard.feature.export.job.pdf.view.MultilineCell;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListItem;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.pdfjet.Align;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

class PdfCellFactory {

    private static final float CELL_WIDTH_DAY = 100;
    private static final float CELL_WIDTH_TIME_LOG = 72;

    private final PdfExportCache cache;

    PdfCellFactory(PdfExportCache cache) {
        this.cache = cache;
    }

    Cell getDayCell() {
        return new CellBuilder(new Cell(cache.getFontBold()))
            .setWidth(CELL_WIDTH_DAY)
            .setText(DateTimeUtils.toWeekDayAndDate(cache.getDateTime()))
            .build();
    }

    List<Cell> getHourRow(int hoursToSkip) {
        List<Cell> cells = new ArrayList<>();
        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += hoursToSkip) {
            cells.add(getHourCell(hour));
        }
        return cells;
    }

    private Cell getHourCell(Integer hour) {
        float cellWidth = (cache.getPage().getWidth() - CELL_WIDTH_DAY) / (DateTimeConstants.HOURS_PER_DAY / 2f);
        return new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(cellWidth)
            .setText(Integer.toString(hour))
            .setForegroundColor(Color.gray)
            .setTextAlignment(Align.CENTER)
            .build();
    }

    List<Cell> getEmptyRow() {
        List<Cell> row = new ArrayList<>();
        row.add(getEmptyCell());
        return row;
    }

    private Cell getEmptyCell() {
        return new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(cache.getPage().getWidth())
            .setText(cache.getContext().getString(R.string.no_data))
            .setBackgroundColor(cache.getColorDivider())
            .setForegroundColor(Color.gray)
            .build();
    }

    List<Cell> getNoteRow(PdfNote pdfNote, boolean appendBorder) {
        ArrayList<Cell> row = new ArrayList<>();

        Cell timeCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(CELL_WIDTH_DAY)
            .setText(Helper.getTimeFormat().print(pdfNote.getDateTime()))
            .setForegroundColor(Color.gray)
            .build();
        if (appendBorder) {
            timeCell.setBorder(Border.TOP, true);
        }
        row.add(timeCell);

        Cell noteCell = new CellBuilder(new MultilineCell(cache.getFontNormal()))
            .setWidth(cache.getPage().getWidth() - CELL_WIDTH_DAY)
            .setText(pdfNote.getNote())
            .setForegroundColor(Color.gray)
            .build();
        if (appendBorder) {
            noteCell.setBorder(Border.TOP, true);
        }
        row.add(noteCell);

        return row;
    }

    @Deprecated
    List<List<Cell>> getNoteRows(List<PdfNote> pdfNotes) {
        List<List<Cell>> rows = new ArrayList<>();
        for (PdfNote pdfNote : pdfNotes) {
            boolean isFirst = pdfNotes.indexOf(pdfNote) == 0;
            List<Cell> row = getNoteRow(pdfNote, isFirst);
            rows.add(row);
        }
        return rows;
    }

    List<Cell> getTableRow(
        CategoryValueListItem[] items,
        int valueIndex,
        String label,
        int backgroundColor
    ) {
        List<Cell> cells = new ArrayList<>();
        float cellWidth = (cache.getPage().getWidth() - CELL_WIDTH_DAY) / (DateTimeConstants.HOURS_PER_DAY / 2f);

        Cell labelCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(CELL_WIDTH_DAY)
            .setText(label)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        cells.add(labelCell);

        for (CategoryValueListItem item : items) {
            Category category = item.getCategory();
            float value = 0;
            switch (valueIndex) {
                case -1: value = item.getValueTotal(); break;
                case 0: value = item.getValueOne(); break;
                case 1: value = item.getValueTwo(); break;
                case 2: value = item.getValueThree(); break;
            }
            int textColor = Color.black;
            if (category == Category.BLOODSUGAR && cache.getConfig().highlightLimits()) {
                if (value > PreferenceStore.getInstance().getLimitHyperglycemia()) {
                    textColor = cache.getColorHyperglycemia();
                } else if (value < PreferenceStore.getInstance().getLimitHypoglycemia()) {
                    textColor = cache.getColorHypoglycemia();
                }
            }
            float customValue = PreferenceStore.getInstance().formatDefaultToCustomUnit(category, value);
            String text = customValue > 0 ? FloatUtils.parseFloat(customValue) : "";

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

    List<Cell> getLogRow(String title, String subtitle, String description, int backgroundColor, int foregroundColor) {
        List<Cell> entryRow = new ArrayList<>();
        float width = cache.getPage().getWidth();

        Cell titleCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(CELL_WIDTH_TIME_LOG)
            .setText(title)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        entryRow.add(titleCell);

        Cell subtitleCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(CELL_WIDTH_DAY)
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

    List<Cell> getLogRow(String title, String subtitle, String description, int backgroundColor) {
        return getLogRow(title, subtitle, description, backgroundColor, Color.black);
    }
}
