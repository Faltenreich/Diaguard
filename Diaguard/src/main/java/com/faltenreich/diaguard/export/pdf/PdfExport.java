package com.faltenreich.diaguard.export.pdf;

import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.export.Export;
import com.faltenreich.diaguard.export.ExportCallback;
import com.faltenreich.diaguard.export.ExportFormat;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.export.pdf.print.PdfChart;
import com.faltenreich.diaguard.export.pdf.print.PdfLog;
import com.faltenreich.diaguard.export.pdf.print.PdfPage;
import com.faltenreich.diaguard.export.pdf.print.PdfPageable;
import com.faltenreich.diaguard.export.pdf.print.PdfTable;
import com.faltenreich.diaguard.export.pdf.print.PdfWeek;

import org.joda.time.Days;

import java.io.File;

public class PdfExport extends AsyncTask<Void, String, File> {

    private static final String TAG = PdfExport.class.getSimpleName();

    private PdfExportConfig config;

    public PdfExport(PdfExportConfig config) {
        this.config = config;
    }

    @Override
    protected File doInBackground(Void... params) {
        File file = Export.getExportFile(ExportFormat.PDF);
        try {
            PdfExportCache cache = new PdfExportCache(config, file);

            PdfPage page = new PdfPage(cache.getPdf());

            while (cache.isDateTimeValid()) {
                if (cache.isDateTimeForNewWeek()) {
                    page = new PdfPage(cache.getPdf());
                    page.draw(new PdfWeek(cache));
                }

                PdfPageable content = null;
                switch (cache.getConfig().getStyle()) {
                    case TABLE:
                        content = new PdfTable(cache, page.getWidth());
                        break;
                    case LOG:
                        content = new PdfLog(cache, page.getWidth());
                        break;
                    case TIMELINE:
                        content = new PdfChart(cache, page.getWidth());
                        break;
                }

                // Page break
                if ((page.getPosition().getY() + content.getHeight()) > page.getEndPoint().getY()) {
                    page = new PdfPage(cache.getPdf());
                }

                if (!page.hasContent()) {
                    content.onNewPage(page);
                }

                page.draw(content);

                publishProgress(String.format("%s %d/%d",
                    DiaguardApplication.getContext().getString(R.string.day),
                    Days.daysBetween(cache.getConfig().getDateStart(), cache.getDateTime()).getDays() + 1,
                    Days.daysBetween(cache.getConfig().getDateStart(), cache.getConfig().getDateEnd()).getDays() + 1)
                );

                cache.setDateTime(cache.getDateTime().plusDays(1));
            }

            cache.getPdf().flush();
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
                callback.onSuccess(file, PdfExportConfig.MIME_TYPE);
            } else {
                callback.onError();
            }
        }
    }
}
