package com.faltenreich.diaguard.feature.export.job.pdf.view;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNote;
import com.faltenreich.diaguard.shared.Helper;
import com.pdfjet.Align;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class CellFactory {

    private static final float CELL_WIDTH_DAY = 100;

    private final PdfExportCache cache;

    public CellFactory(PdfExportCache cache) {
        this.cache = cache;
    }

    public Cell getDayCell() {
        return new CellBuilder(new Cell(cache.getFontBold()))
            .setWidth(CELL_WIDTH_DAY)
            .setText(DateTimeUtils.toWeekDayAndDate(cache.getDateTime()))
            .build();
    }

    public List<Cell> getHourCells(int hoursToSkip) {
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

    private Cell getEmptyCell() {
        return new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(cache.getPage().getWidth())
            .setText(cache.getContext().getString(R.string.no_data))
            .setBackgroundColor(cache.getColorDivider())
            .setForegroundColor(Color.gray)
            .build();
    }

    public List<Cell> getEmptyCells() {
        List<Cell> row = new ArrayList<>();
        row.add(getEmptyCell());
        return row;
    }

    @Deprecated
    public List<List<Cell>> createRowsForNotes(List<PdfNote> pdfNotes) {
        List<List<Cell>> rows = new ArrayList<>();
        for (PdfNote note : pdfNotes) {
            boolean isFirst = pdfNotes.indexOf(note) == 0;
            List<Cell> row = createRowForNote(note, isFirst);
            rows.add(row);
        }
        return rows;
    }

    public List<Cell> createRowForNote(PdfNote pdfNote, boolean appendBorder) {
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
}
