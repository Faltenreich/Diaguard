package com.faltenreich.diaguard.export.pdf;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.export.Export;
import com.faltenreich.diaguard.export.ExportCallback;
import com.faltenreich.diaguard.export.ExportFormat;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Point;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.File;
import java.lang.ref.WeakReference;

public class PdfExport extends AsyncTask<Void, String, File> {

    private static final String TAG = PdfExport.class.getSimpleName();

    private static final float PADDING_PARAGRAPH = 20;

    private PdfExportConfig config;

    public PdfExport(PdfExportConfig config) {
        this.config = config;
    }

    @Override
    protected File doInBackground(Void... params) {
        File file = Export.getExportFile(ExportFormat.PDF);
        try {
            WeakReference<Context> contextReference = config.getContextReference();

            Pdf pdf = new Pdf(file, config);

            Font fontNormal = new Font(pdf, CoreFont.HELVETICA);
            Font fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);

            DateTime dateIteration = config.getDateStart();

            PdfPage page = new PdfPage(pdf);
            Point currentPosition = new PdfWeekHeader(contextReference, dateIteration, fontNormal, fontBold).drawOn(page);

            // One day after last chosen day
            DateTime dateAfter = config.getDateEnd().plusDays(1);

            // Day by day
            while (dateIteration.isBefore(dateAfter)) {
                // title bar for new week
                if (dateIteration.isAfter(config.getDateStart()) && dateIteration.getDayOfWeek() == 1) {
                    page = new PdfPage(pdf);
                    currentPosition = new PdfWeekHeader(contextReference, dateIteration, fontNormal, fontBold).drawOn(page);
                }

                PdfTable table = new PdfTable(config, dateIteration, pdf, page);

                // Page break
                if ((currentPosition.getY() + table.getHeight() + PADDING_PARAGRAPH) > page.getEndPoint().getY()) {
                    page = new PdfPage(pdf);
                    currentPosition = new PdfWeekHeader(contextReference, dateIteration, fontNormal, fontBold).drawOn(page);
                }

                table.setPosition(currentPosition.getX(), currentPosition.getY());
                currentPosition = table.drawOn(page);
                currentPosition.setY(currentPosition.getY() + PADDING_PARAGRAPH);

                publishProgress(String.format("%s %d/%d",
                    DiaguardApplication.getContext().getString(R.string.day),
                    Days.daysBetween(config.getDateStart(), dateIteration).getDays() + 1,
                    Days.daysBetween(config.getDateStart(), config.getDateEnd()).getDays() + 1)
                );

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
        ExportCallback callback = config.getCallback();
        if (callback != null) {
            callback.onProgress(message[0]);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        ExportCallback callback = config.getCallback();
        if (callback != null) {
            if (file != null) {
                callback.onSuccess(file, PdfMeta.PDF_MIME_TYPE);
            } else {
                callback.onError();
            }
        }
    }
}
