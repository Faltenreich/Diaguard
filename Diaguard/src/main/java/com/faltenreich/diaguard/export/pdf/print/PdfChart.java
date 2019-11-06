package com.faltenreich.diaguard.export.pdf.print;

import android.util.Log;

import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.view.SizedBox;
import com.faltenreich.diaguard.export.pdf.view.SizedTable;
import com.pdfjet.Color;
import com.pdfjet.Point;

import java.util.List;

public class PdfChart implements PdfPrintable {

    private static final String TAG = PdfLog.class.getSimpleName();
    private static final float PADDING_PARAGRAPH = 20;

    private PdfExportCache cache;
    private float width;
    private SizedBox chart;
    private SizedTable table;

    public PdfChart(PdfExportCache cache, float width) {
        this.cache = cache;
        this.width = width;
        this.chart = new SizedBox(width, width / 3);
        this.table = new SizedTable();
    }

    @Override
    public float getHeight() {
        return chart.getHeight() +
            table.getHeight() +
            PADDING_PARAGRAPH;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        try {
            drawChart(page, position);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    private void drawHeader() throws Exception {
        // TODO
    }

    private void drawChart(PdfPage page, Point position) throws Exception {
        chart.setPosition(position.getX(), position.getY());
        chart.drawOn(page);

        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(cache.getDateTime());
        for (Entry entry : entries) {

        }

        Point point = new Point(10, 10);
        point.setColor(Color.red);
        point.placeIn(chart);
        point.drawOn(page);
    }

    private void drawTable() throws Exception {
        // TODO
    }
}
