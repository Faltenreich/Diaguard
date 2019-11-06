package com.faltenreich.diaguard.export.pdf.print;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.view.SizedBox;
import com.faltenreich.diaguard.export.pdf.view.SizedTable;
import com.pdfjet.Point;

import java.util.ArrayList;
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
        drawChart(page, position);
    }

    private void drawHeader() throws Exception {
        // TODO
    }

    private void drawChart(PdfPage page, Point position) throws Exception {
        chart.setPosition(position.getX(), position.getY());
        chart.drawOn(page);

        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(cache.getDateTime());
        List<BloodSugar> bloodSugars = new ArrayList<>();
        List<Measurement> otherMeasurements = new ArrayList<>();
        for (Entry entry : entries) {
            List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
            for (Measurement measurement : measurements) {
                if (measurement instanceof BloodSugar) {
                    bloodSugars.add((BloodSugar) measurement);
                } else {
                    otherMeasurements.add(measurement);
                }
            }
        }

        for (BloodSugar bloodSugar : bloodSugars) {
            Entry entry = bloodSugar.getEntry();
            int minute = entry.getDate().getMinuteOfDay();
            float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, bloodSugar.getMgDl());
            // TODO
            float x = 0;
            float y = 0;

            Point point = new Point(x, y);
            // point.setColor(Color.red);
            point.placeIn(chart);
            point.drawOn(page);
        }
    }

    private void drawTable() throws Exception {
        // TODO
    }
}
