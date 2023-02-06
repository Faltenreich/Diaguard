package com.faltenreich.diaguard.feature.export.job.pdf.print;

public interface PdfPrintable {

    float getHeight();

    default float getLabelWidth() {
        return 100;
    }

    void drawOn(PdfPage pagen) throws Exception;
}
