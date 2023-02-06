package com.faltenreich.diaguard.feature.export.job.pdf.print;

public interface PdfPrintable {

    default float getLabelWidth() {
        return 100;
    }

    void drawOn(PdfPage pagen) throws Exception;
}
