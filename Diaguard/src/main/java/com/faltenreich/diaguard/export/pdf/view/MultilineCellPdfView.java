package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.util.StringUtils;
import com.pdfjet.Cell;
import com.pdfjet.Font;
import com.pdfjet.Page;

import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;

public class MultilineCellPdfView extends Cell {

    private int characterCount;

    public MultilineCellPdfView(Font font, String text, float width) {
        super(font);
        setWidth(width);
        setText(text);
        characterCount = font.getFitChars(text, width);
    }

    @Override
    public String getText() {
        return WordUtils.wrap(super.getText(), characterCount);
    }

    @Override
    public float getHeight() {
        float height = font.getBodyHeight();
        String text = getText();

        if (text != null) {
            String[] wrappedTexts = text.split(StringUtils.newLine());
            if (wrappedTexts.length > 1) {
                return (height * wrappedTexts.length) + top_padding + bottom_padding;
            }
        }
        return height + top_padding + bottom_padding;
    }

    @Override
    protected void paint(Page page, float x, float y, float w, float h) throws Exception {
        page.setPenColor(getPenColor());
        page.setPenWidth(lineWidth);
        drawText(page, x, y);
    }

    private void drawText(Page page, float x, float y) throws IOException {
        String wrappedText = WordUtils.wrap(super.getText(), this.characterCount);
        String[] lines = wrappedText.split(StringUtils.newLine());

        float x_text = x + left_padding;
        float y_text = y + font.getAscent() + top_padding;

        for (String line : lines) {
            page.drawString(font, line, x_text, y_text);
            y_text += font.getBodyHeight();
        }
    }
}