package com.faltenreich.diaguard.export.pdf;

import android.util.Log;

import com.faltenreich.diaguard.export.pdf.print.Pdf;
import com.faltenreich.diaguard.export.pdf.print.PdfPage;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Point;

import org.joda.time.DateTime;

import java.io.File;

public class PdfExportCache {

    private static final String TAG = PdfExportCache.class.getSimpleName();

    private PdfExportConfig config;
    private Font fontNormal;
    private Font fontBold;

    private Pdf pdf;
    private PdfPage page;
    private DateTime dateTime;
    private Point position;

    public PdfExportCache(PdfExportConfig config, File file) throws Exception {
        this.config = config;

        this.pdf = new Pdf(file, config);
        this.fontNormal = new Font(pdf, CoreFont.HELVETICA);
        this.fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);

        this.dateTime = config.getDateStart();
        this.page = new PdfPage(pdf);
        this.position = new Point();
    }

    public PdfExportConfig getConfig() {
        return config;
    }

    public Font getFontNormal() {
        return fontNormal;
    }

    public Font getFontBold() {
        return fontBold;
    }

    public Pdf getPdf() {
        return pdf;
    }

    public PdfPage getPage() {
        return page;
    }

    public void newPage() {
        try {
            this.page = new PdfPage(pdf);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public boolean isDateTimeValid() {
        return dateTime.isBefore(config.getDateEnd().plusDays(1));
    }

    public boolean isDateTimeForNewWeek() {
        return dateTime.isAfter(config.getDateStart()) && dateTime.getDayOfWeek() == 1;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
