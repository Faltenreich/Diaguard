package com.faltenreich.diaguard.export.pdf;

import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Point;

import java.io.File;

public class PdfExportCache {

    private PdfExportConfig config;
    private Pdf pdf;
    private Font fontNormal;
    private Font fontBold;
    private Point currentPosition;

    PdfExportCache(PdfExportConfig config, File file) throws Exception {
        this.config = config;
        this.pdf = new Pdf(file, config);
        this.fontNormal = new Font(pdf, CoreFont.HELVETICA);
        this.fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);
        this.currentPosition = new Point();
    }

    PdfExportConfig getConfig() {
        return config;
    }

    Pdf getPdf() {
        return pdf;
    }

    Font getFontNormal() {
        return fontNormal;
    }

    Font getFontBold() {
        return fontBold;
    }

    Point getCurrentPosition() {
        return currentPosition;
    }
}
