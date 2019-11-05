package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.util.StringUtils;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Font;
import com.pdfjet.Page;

import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;

public class MultilineCell extends Cell {

    public MultilineCell(Font font) {
        super(font);
    }

    @Override
    public String getText() {
        return WordUtils.wrap(super.getText(), font.getFitChars(text, width));
    }

    @Override
    public float getHeight() {
        float fontHeight = font.getBodyHeight();
        String text = getText();

        if (text != null) {
            String[] lines = text.split(StringUtils.newLine());
            if (lines.length > 1) {
                return (fontHeight * lines.length) + top_padding + bottom_padding;
            }
        }
        return fontHeight + top_padding + bottom_padding;
    }

    @Override
    protected void paint(Page page, float x, float y, float w, float h) throws Exception {
        drawBackground(page, x, y, w, h);
        drawText(page, x, y);
        drawBorders(page, x, y, w, h);
    }

    private void drawText(Page page, float x, float y) throws IOException {
        page.setBrushColor(getPenColor());

        String text = getText();
        String[] lines = text.split(StringUtils.newLine());

        float x_text = x + left_padding;
        float y_text = y + font.getAscent() + top_padding;

        for (String line : lines) {
            page.drawString(font, line, x_text, y_text);
            y_text += font.getBodyHeight();
        }
    }

    private void drawBackground(Page page, float x, float y, float w, float h) throws IOException {
        page.setBrushColor(getBgColor());

        page.fillRect(x, y + lineWidth / 2, w, h + lineWidth);
    }

    private void drawBorders(Page page, float x, float y, float cell_w, float cell_h) throws Exception {
        if (getBorder(Border.TOP) &&
            getBorder(Border.BOTTOM) &&
            getBorder(Border.LEFT) &&
            getBorder(Border.RIGHT)
        ) {
            page.drawRect(x, y, cell_w, cell_h);
        } else {
            if (getBorder(Border.TOP)) {
                page.moveTo(x, y);
                page.lineTo(x + cell_w, y);
                page.strokePath();
            }
            if (getBorder(Border.BOTTOM)) {
                page.moveTo(x, y + cell_h);
                page.lineTo(x + cell_w, y + cell_h);
                page.strokePath();
            }
            if (getBorder(Border.LEFT)) {
                page.moveTo(x, y);
                page.lineTo(x, y + cell_h);
                page.strokePath();
            }
            if (getBorder(Border.RIGHT)) {
                page.moveTo(x + cell_w, y);
                page.lineTo(x + cell_w, y + cell_h);
                page.strokePath();
            }
        }
    }
}