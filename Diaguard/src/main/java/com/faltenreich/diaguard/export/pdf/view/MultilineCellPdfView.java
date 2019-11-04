package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.util.StringUtils;
import com.pdfjet.Cell;
import com.pdfjet.Font;
import com.pdfjet.Page;

import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;

public class MultilineCellPdfView extends Cell {

    public MultilineCellPdfView(Font font) {
        super(font);
    }

    @Override
    public String getText() {
        return WordUtils.wrap(super.getText(), font.getFitChars(text, width));
    }

    @Override
    public float getHeight() {
        float height = font.getBodyHeight();
        String text = getText();

        if (text != null) {
            String[] lines = text.split(StringUtils.newLine());
            if (lines.length > 1) {
                return (height * lines.length) + top_padding + bottom_padding;
            }
        }
        return height + top_padding + bottom_padding;
    }

    @Override
    protected void paint(Page page, float x, float y, float w, float h) throws Exception {
        drawBackground(page, x, y, w, h);
        drawText(page, x, y);
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
}