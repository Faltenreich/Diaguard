package com.faltenreich.diaguard.export.pdf;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.export.Export;
import com.faltenreich.diaguard.export.ExportCallback;
import com.faltenreich.diaguard.export.ExportFormat;
import com.faltenreich.diaguard.util.Helper;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.PDF;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileOutputStream;

public class PdfExport extends AsyncTask<Void, String, File> {

    private static final String TAG = PdfExport.class.getSimpleName();

    private static final float PADDING_PARAGRAPH = 20;
    private static final float PADDING_LINE = 3;

    private PdfExportConfig config;

    private Font fontNormal;
    private Font fontBold;

    public PdfExport(PdfExportConfig config) {
        this.config = config;
    }

    private Context getContext() {
        return config.getContextReference().get();
    }

    private ExportCallback getCallback() {
        return config.getCallback();
    }

    @Override
    protected File doInBackground(Void... params) {
        File file = Export.getExportFile(ExportFormat.PDF);
        try {
            Point currentPosition;

            PDF pdf = new PDF(new FileOutputStream(file));
            pdf.setTitle(String.format("%s %s",
                    getContext().getString(R.string.app_name),
                    DiaguardApplication.getContext().getString(R.string.export)));
            pdf.setSubject(String.format("%s %s: %s - %s",
                    DiaguardApplication.getContext().getString(R.string.app_name),
                    DiaguardApplication.getContext().getString(R.string.export),
                    Helper.getDateFormat().print(config.getDateStart()),
                    Helper.getDateFormat().print(config.getDateEnd())));
            pdf.setAuthor(DiaguardApplication.getContext().getString(R.string.app_name));

            fontNormal = new Font(pdf, CoreFont.HELVETICA);
            fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);

            DateTime dateIteration = config.getDateStart();

            PdfPage page = createPage(pdf);
            currentPosition = drawWeekBar(page, dateIteration);

            // One day after last chosen day
            DateTime dateAfter = config.getDateEnd().plusDays(1);

            // Day by day
            while (dateIteration.isBefore(dateAfter)) {
                // title bar for new week
                if (dateIteration.isAfter(config.getDateStart()) && dateIteration.getDayOfWeek() == 1) {
                    page = createPage(pdf);
                    currentPosition = drawWeekBar(page, dateIteration);
                }

                PdfTable table = new PdfTable(getContext(), pdf, page, dateIteration, config.getCategories(), config.isExportNotes(), config.isExportTags(), config.isExportFood(), config.isSplitInsulin());

                // Page break
                if ((currentPosition.getY() + table.getHeight() + PADDING_PARAGRAPH) > page.getEndPoint().getY()) {
                    page = new PdfPage(pdf);
                    currentPosition = drawWeekBar(page, dateIteration);
                }

                table.setPosition(currentPosition.getX(), currentPosition.getY());
                currentPosition = table.drawOn(page);
                currentPosition.setY(currentPosition.getY() + PADDING_PARAGRAPH);

                publishProgress(String.format("%s %d/%d",
                        DiaguardApplication.getContext().getString(R.string.day),
                        Days.daysBetween(config.getDateStart(), dateIteration).getDays() + 1,
                        Days.daysBetween(config.getDateStart(), config.getDateEnd()).getDays() + 1));

                dateIteration = dateIteration.plusDays(1);
            }

            pdf.flush();
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }

        return file;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(String... message) {
        if (getCallback() != null) {
            getCallback().onProgress(message[0]);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (getCallback() != null) {
            if (file != null) {
                getCallback().onSuccess(file, PdfMeta.PDF_MIME_TYPE);
            } else {
                getCallback().onError();
            }
        }
    }

    private PdfPage createPage(PDF pdf) {
        try {
            return new PdfPage(pdf);
        } catch (Exception e) {
            Log.e(TAG, "Failed to create new page");
            return null;
        }
    }

    private Point drawWeekBar(PdfPage page, DateTime day) {
        DateTime weekStart = day.withDayOfWeek(1);
        Point currentPosition = page.getStartPoint();

        TextLine week = new TextLine(fontBold);
        week.setPosition(currentPosition.getX(), currentPosition.getY());
        week.setFontSize(15f);
        week.setText(String.format("%s %d",
                DiaguardApplication.getContext().getString(R.string.calendarweek),
                weekStart.getWeekOfWeekyear()));
        try {
            week.drawOn(page);
            currentPosition.setY(currentPosition.getY() + week.getHeight() + PADDING_LINE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to draw TextLine");
        }

        DateTime weekEnd = weekStart.withDayOfWeek(DateTimeConstants.SUNDAY);
        TextLine interval = new TextLine(fontNormal);
        interval.setPosition(currentPosition.getX(), currentPosition.getY());
        interval.setText(String.format("%s - %s",
                DateTimeFormat.mediumDate().print(weekStart),
                DateTimeFormat.mediumDate().print(weekEnd)));
        try {
            interval.drawOn(page);
            currentPosition.setY(currentPosition.getY() + interval.getHeight() + PADDING_PARAGRAPH);
        } catch (Exception e) {
            Log.e(TAG, "Failed to draw TextLine");
        }

        return currentPosition;
    }
}
