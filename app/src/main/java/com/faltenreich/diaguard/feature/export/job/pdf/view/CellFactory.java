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
import com.pdfjet.Font;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class CellFactory {

    private static final float CELL_WIDTH_DAY = 100;

    public static Cell getDayCell(DateTime dateTime, Font font) {
        return new CellBuilder(new Cell(font))
            .setWidth(CELL_WIDTH_DAY)
            .setText(DateTimeUtils.toWeekDayAndDate(dateTime))
            .build();
    }

    public static List<Cell> getHourCells(Font font, float pageWidth, int hoursToSkip) {
        List<Cell> cells = new ArrayList<>();
        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += hoursToSkip) {
            cells.add(getHourCell(hour, font, pageWidth));
        }
        return cells;
    }

    private static Cell getHourCell(Integer hour, Font font, float pageWidth) {
        float cellWidth = (pageWidth - CELL_WIDTH_DAY) / (DateTimeConstants.HOURS_PER_DAY / 2f);
        return new CellBuilder(new Cell(font))
            .setWidth(cellWidth)
            .setText(Integer.toString(hour))
            .setForegroundColor(Color.gray)
            .setTextAlignment(Align.CENTER)
            .build();
    }

    private static Cell getEmptyCell(PdfExportCache cache) {
        return new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(cache.getPage().getWidth())
            .setText(cache.getContext().getString(R.string.no_data))
            .setBackgroundColor(cache.getColorDivider())
            .setForegroundColor(Color.gray)
            .build();
    }

    public static List<Cell> createEmptyRow(PdfExportCache cache) {
        List<Cell> row = new ArrayList<>();
        row.add(getEmptyCell(cache));
        return row;
    }

    @Deprecated
    public static List<List<Cell>> createRowsForNotes(PdfExportCache cache, List<PdfNote> pdfNotes, float labelWidth) {
        List<List<Cell>> rows = new ArrayList<>();
        for (PdfNote note : pdfNotes) {
            boolean isFirst = pdfNotes.indexOf(note) == 0;
            List<Cell> row = createRowForNote(cache, note, labelWidth, isFirst);
            rows.add(row);
        }
        return rows;
    }

    public static List<Cell> createRowForNote(PdfExportCache cache, PdfNote pdfNote, float labelWidth, boolean appendBorder) {
        ArrayList<Cell> row = new ArrayList<>();

        Cell timeCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(labelWidth)
            .setText(Helper.getTimeFormat().print(pdfNote.getDateTime()))
            .setForegroundColor(Color.gray)
            .build();
        if (appendBorder) {
            timeCell.setBorder(Border.TOP, true);
        }
        row.add(timeCell);

        Cell noteCell = new CellBuilder(new MultilineCell(cache.getFontNormal()))
            .setWidth(cache.getPage().getWidth() - labelWidth)
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
