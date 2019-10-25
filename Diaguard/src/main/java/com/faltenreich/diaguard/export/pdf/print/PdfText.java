package com.faltenreich.diaguard.export.pdf.print;

import com.pdfjet.Page;
import com.pdfjet.Paragraph;
import com.pdfjet.Text;

import java.util.List;

public class PdfText extends Text {

    private float height;

    public PdfText(List<Paragraph> paragraphs) throws Exception {
        super(paragraphs);
    }

    public float getHeight() {
        return height;
    }

    @Override
    public float[] drawOn(Page page, boolean draw) throws Exception {
        float[] points = super.drawOn(page, draw);
        float startY = getBeginParagraphPoints().get(0)[1];
        float endY = points[1];
        height = endY - startY;
        return points;
    }
}
