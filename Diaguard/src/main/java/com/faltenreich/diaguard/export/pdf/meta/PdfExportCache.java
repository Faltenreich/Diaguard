package com.faltenreich.diaguard.export.pdf.meta;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.export.pdf.print.Pdf;
import com.faltenreich.diaguard.export.pdf.print.PdfPage;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;

import org.joda.time.DateTime;

import java.io.File;

public class PdfExportCache {

    private static final float FONT_SIZE_HEADER = 14f;

    private PdfExportConfig config;
    private Pdf pdf;
    private PdfPage page;
    private DateTime dateTime;

    private Font fontNormal;
    private Font fontBold;
    private Font fontHeader;
    private int colorDivider;
    private int colorHyperglycemia;
    private int colorHypoglycemia;

    public PdfExportCache(PdfExportConfig config, File file) throws Exception {
        this.config = config;
        this.pdf = new Pdf(file, config);

        this.fontNormal = new Font(pdf, CoreFont.HELVETICA);
        this.fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);
        this.fontHeader = new Font(pdf, CoreFont.HELVETICA_BOLD);
        this.fontHeader.setSize(FONT_SIZE_HEADER);
        this.colorDivider = ContextCompat.getColor(getContext(), R.color.background_light_primary);
        this.colorHyperglycemia = ContextCompat.getColor(getContext(), R.color.red);
        this.colorHypoglycemia = ContextCompat.getColor(getContext(), R.color.blue);

        this.dateTime = config.getDateStart();
        newPage();
    }

    public PdfPage getPage() {
        return page;
    }

    public void newPage() throws Exception {
        page = new PdfPage(this);
    }

    public Context getContext() {
        return config.getContext();
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

    public Font getFontHeader() {
        return fontHeader;
    }

    public int getColorDivider() {
        return colorDivider;
    }

    public int getColorHyperglycemia() {
        return colorHyperglycemia;
    }

    public int getColorHypoglycemia() {
        return colorHypoglycemia;
    }

    public Pdf getPdf() {
        return pdf;
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
}
