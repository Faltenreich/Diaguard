package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Align;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

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
        this.backgroundColor = Color.transparent;
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

    // TODO: Localize dateString
    public CellBuilder setText(DateTime dateTime) {
        String weekDayString = DateTimeFormat.forPattern("E").print(dateTime);
        String dateString = DateTimeFormat.forPattern("dd.MM").print(dateTime);
        return setText(String.format("%s %s", weekDayString, dateString));
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
}
