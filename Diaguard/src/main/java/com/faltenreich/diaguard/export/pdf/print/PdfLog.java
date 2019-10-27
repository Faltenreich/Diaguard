package com.faltenreich.diaguard.export.pdf.print;

import android.content.Context;
import android.util.Log;

import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.export.pdf.view.DayCellPdfView;
import com.faltenreich.diaguard.export.pdf.view.LabelCellPdfView;
import com.faltenreich.diaguard.export.pdf.view.SizedTablePdfView;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Point;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class PdfLog implements PdfPrintable {

    private static final String TAG = PdfLog.class.getSimpleName();
    private static final float PADDING_PARAGRAPH = 20;
    public static final float LABEL_WIDTH = 120;

    private SizedTablePdfView table;
    private float width;

    public PdfLog(PdfExportCache cache, float width) {
        this.table = new SizedTablePdfView();
        this.width = width;
        init(cache);
    }

    @Override
    public float getHeight() {
        float height = table.getHeight();
        return height > 0 ? height + PADDING_PARAGRAPH : 0f;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        table.setLocation(position.getX(), position.getY());
        table.drawOn(page);
    }

    private void init(PdfExportCache cache) {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContextReference().get();
        float cellWidth = (width - LABEL_WIDTH) / (DateTimeConstants.HOURS_PER_DAY / 2f);

        List<List<Cell>> data = new ArrayList<>();
        List<Cell> headerRow = new ArrayList<>();
        headerRow.add(new DayCellPdfView(cache.getFontBold(), cache.getDateTime()));
        data.add(headerRow);

        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(cache.getDateTime());
        for (Entry entry : entries) {
            List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
            entry.setMeasurementCache(measurements);
        }

        int row = 0;
        for (Entry entry : entries) {
            int backgroundColor = row % 2 == 0 ? cache.getColorDivider() : Color.white;
            List<Cell> entryRow = new ArrayList<>();

            String label = entry.getDate().toString("HH:mm");
            entryRow.add(new LabelCellPdfView(cache.getFontNormal(), label, backgroundColor));

            data.add(entryRow);
            row++;
        }

        try {
            table.setData(data);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }
}
