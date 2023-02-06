package com.faltenreich.diaguard.feature.export.job.pdf;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.feature.export.job.pdf.print.PdfPage;
import com.faltenreich.diaguard.feature.export.job.pdf.print.PdfPageFactory;
import com.faltenreich.diaguard.feature.export.job.pdf.print.PdfPrintable;
import com.faltenreich.diaguard.feature.export.job.pdf.print.PdfPrintableFactory;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.File;

public class PdfExport extends AsyncTask<Void, String, Pair<File, String>> {

    private static final String TAG = PdfExport.class.getSimpleName();

    private final PdfExportConfig config;

    public PdfExport(PdfExportConfig config) {
        this.config = config;
    }

    @Override
    protected Pair<File, String> doInBackground(Void... params) {
        try {
            File file = Export.getExportFile(config);
            PdfExportCache cache = new PdfExportCache(config, file);
            while (cache.isDateTimeValid()) {
                boolean isNewPage = config.getDateStart().equals(cache.getDateTime())
                    || cache.isDateTimeForNewWeek();
                if (isNewPage) {
                    PdfPage page = PdfPageFactory.createPage(cache);
                    // Skip empty page
                    if (page == null) {
                        DateTime nextWeek = DateTimeUtils.atStartOfWeek(cache.getDateTime().plusWeeks(1));
                        cache.setDateTime(nextWeek);
                        continue;
                    }
                    cache.setPage(page);
                }

                PdfPrintable printable = PdfPrintableFactory.createPrintable(cache);
                if (printable != null) {
                    printable.drawOn(cache.getPage());
                }

                publishProgress(cache);
                cache.setDateTime(cache.getDateTime().plusDays(1));
            }

            boolean hasContent = cache.getPage() != null;
            if (hasContent) {
                cache.clear();
                return new Pair<>(file, null);
            } else {
                return new Pair<>(null, config.getContext().getString(R.string.no_data));
            }
        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
            return new Pair<>(null, config.getContext().getString(R.string.error_unexpected));
        }
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
    protected void onPostExecute(Pair<File, String> result) {
        super.onPostExecute(result);
        ExportCallback callback = config.getCallback();
        if (callback != null) {
            if (result.first != null) {
                callback.onSuccess(result.first, FileType.PDF.mimeType);
            } else {
                callback.onError(result.second);
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private void publishProgress(PdfExportCache cache) {
        publishProgress(String.format("%s %d/%d",
            config.getContext().getString(R.string.day),
            Days.daysBetween(cache.getConfig().getDateStart(), cache.getDateTime()).getDays() + 1,
            Days.daysBetween(cache.getConfig().getDateStart(), cache.getConfig().getDateEnd()).getDays() + 1)
        );
    }
}
