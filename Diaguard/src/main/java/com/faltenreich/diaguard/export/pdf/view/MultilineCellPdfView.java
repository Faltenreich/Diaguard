package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Cell;
import com.pdfjet.Font;
import com.pdfjet.Page;

import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;

public class MultilineCellPdfView extends Cell {

    private final int characterCount;

    public MultilineCellPdfView(Font font, String content, int characterCount) {
        super(font, content);
        this.characterCount = characterCount;
    }

    @Override
    public String getText() {
        return WordUtils.wrap(super.getText(), this.characterCount);
    }

    @Override
    public float getHeight() {
        float height = this.font.getBodyHeight();

        String text = getText();
        if (text != null) {
            String[] wrappedTexts = text.split(System.getProperty("line.separator"));

            if (wrappedTexts.length > 1) {
                return (height * wrappedTexts.length) + this.top_padding + this.bottom_padding;
            }
        }
        return height + this.top_padding + this.bottom_padding;
    }

    @Override
    protected void paint(Page page, float x, float y, float w, float h) throws Exception {
        page.setPenColor(this.getPenColor());
        page.setPenWidth(this.lineWidth);
        drawText(page, x, y);
    }

    private void drawText(Page page, float x, float y) throws IOException {
        String wrappedText = WordUtils.wrap(super.getText(), this.characterCount);
        String[] lines = wrappedText.split(System.getProperty("line.separator"));

        float x_text = x + this.left_padding;
        float y_text = y + this.font.getAscent() + this.top_padding;

        for (String line : lines) {
            page.drawString(this.font, line, x_text, y_text);
            y_text += this.font.getBodyHeight();
        }
    }
}