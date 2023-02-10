package com.faltenreich.diaguard.feature.export.job.pdf.view;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNote;
import com.faltenreich.diaguard.shared.Helper;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import java.util.ArrayList;
import java.util.List;

public class CellFactory {

    private static Cell createEmptyCell(PdfExportCache cache) {
        return new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(cache.getPage().getWidth())
            .setText(cache.getContext().getString(R.string.no_data))
            .setBackgroundColor(cache.getColorDivider())
            .setForegroundColor(Color.gray)
            .build();
    }

    public static List<Cell> createEmptyRow(PdfExportCache cache) {
        List<Cell> row = new ArrayList<>();
        row.add(createEmptyCell(cache));
        return row;
    }

    public static List<List<Cell>> createRowsForNotes(PdfExportCache cache, List<PdfNote> pdfNotes, float labelWidth) {
        List<List<Cell>> rows = new ArrayList<>();
        for (PdfNote note : pdfNotes) {
            boolean isFirst = pdfNotes.indexOf(note) == 0;

            ArrayList<Cell> noteCells = new ArrayList<>();

            Cell timeCell = new CellBuilder(new Cell(cache.getFontNormal()))
                .setWidth(labelWidth)
                .setText(Helper.getTimeFormat().print(note.getDateTime()))
                .setForegroundColor(Color.gray)
                .build();
            if (isFirst) {
                timeCell.setBorder(Border.TOP, true);
            }
            noteCells.add(timeCell);

            Cell noteCell = new CellBuilder(new MultilineCell(cache.getFontNormal()))
                .setWidth(cache.getPage().getWidth() -labelWidth)
                .setText(note.getNote())
                .setForegroundColor(Color.gray)
                .build();
            if (isFirst) {
                noteCell.setBorder(Border.TOP, true);
            }
            noteCells.add(noteCell);

            rows.add(noteCells);
        }
        return rows;
    }
}
