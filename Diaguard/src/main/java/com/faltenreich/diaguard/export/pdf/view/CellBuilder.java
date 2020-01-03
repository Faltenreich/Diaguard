package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.pdfjet.Align;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import java.util.ArrayList;
import java.util.List;

public class CellBuilder {

    private Cell cell;
    private float width;
    private String text;
    private int textAlignment;
    private int backgroundColor;
    private int foregroundColor;

    public CellBuilder(Cell cell) {
        this.cell = cell;
        this.textAlignment = Align.LEFT;
        this.backgroundColor = Color.white;
        this.foregroundColor = Color.black;
    }

    public CellBuilder setWidth(float width) {
        this.width = width;
        return this;
    }

    public CellBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public CellBuilder setTextAlignment(int textAlignment) {
        this.textAlignment = textAlignment;
        return this;
    }

    public CellBuilder setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public CellBuilder setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
        return this;
    }

    public Cell build() {
        cell.setNoBorders();
        cell.setWidth(width);
        cell.setText(text);
        cell.setTextAlignment(textAlignment);
        cell.setBgColor(backgroundColor);
        cell.setFgColor(foregroundColor);
        return cell;
    }

    private static Cell emptyCell(PdfExportCache cache) {
        return new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(cache.getPage().getWidth())
            .setText(cache.getContext().getString(R.string.no_data))
            .setBackgroundColor(cache.getColorDivider())
            .setForegroundColor(Color.gray)
            .build();
    }

    public static List<Cell> emptyRow(PdfExportCache cache) {
        List<Cell> row = new ArrayList<>();
        row.add(CellBuilder.emptyCell(cache));
        return row;
    }
}
