package com.faltenreich.diaguard.export.pdf.print;

import android.util.Log;

import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.pdfjet.Box;
import com.pdfjet.Color;
import com.pdfjet.Point;

import java.util.List;

public class PdfChart implements PdfPrintable {

    private static final String TAG = PdfLog.class.getSimpleName();

    private PdfExportCache cache;
    private float width;
    private Box box;

    public PdfChart(PdfExportCache cache, float width) {
        this.cache = cache;
        this.width = width;
        this.box = new Box(0, 0, width, getHeight());
    }

    @Override
    public float getHeight() {
        return width / 3;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        box.setPosition(position.getX(), position.getY());
        box.drawOn(page);

        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(cache.getDateTime());
        for (Entry entry : entries) {

        }

        Point point = new Point(10, 10);
        point.setColor(Color.red);
        try {
            point.placeIn(box);
            point.drawOn(page);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }
}
