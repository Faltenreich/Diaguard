package com.faltenreich.diaguard.export.pdf.print;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.view.SizedBox;
import com.faltenreich.diaguard.export.pdf.view.SizedTable;
import com.pdfjet.Color;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class PdfChart implements PdfPrintable {

    private static final float PADDING_PARAGRAPH = 20;
    private static final float POINT_RADIUS = 5;
    private static final float CHART_PADDING = 8;
    private static final int X_MAX = DateTimeConstants.MINUTES_PER_DAY;

    private PdfExportCache cache;
    private SizedBox chart;
    private SizedTable table;

    public PdfChart(PdfExportCache cache, float width) {
        this.cache = cache;
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

        drawHeader();
        drawChart(page, position, bloodSugars);
        drawTable(page, position, otherMeasurements);
    }

    private void drawHeader() throws Exception {
        // TODO
    }

    private void drawChart(PdfPage page, Point position, List<BloodSugar> bloodSugars) throws Exception {
        chart.setPosition(position.getX(), position.getY());
        chart.drawOn(page);

        float chartWidth = chart.getWidth();
        float chartHeight = chart.getHeight();

        float yMax = 250;
        for (BloodSugar bloodSugar : bloodSugars) {
            if (bloodSugar.getMgDl() > yMax) {
                yMax = bloodSugar.getMgDl();
            }
        }

        TextLine text = new TextLine(cache.getFontNormal());
        float yLabel = chartHeight - text.getHeight();
        while (yLabel >= CHART_PADDING) {
            text = new TextLine(cache.getFontNormal());
            text.setText("" + yLabel);
            text.setPosition(CHART_PADDING, yLabel);
            text.placeIn(chart);
            text.drawOn(page);
            yLabel = yLabel - 25;
        }

        for (BloodSugar bloodSugar : bloodSugars) {
            Entry entry = bloodSugar.getEntry();
            float minute = entry.getDate().getMinuteOfDay();
            float value = bloodSugar.getMgDl();
            float x = (minute / X_MAX) * chartWidth;
            float y = chartHeight - (value / yMax) * chartHeight;

            Point point = new Point(x, y);
            int color = Color.black;
            if (cache.getConfig().isHighlightLimits()) {
                if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                    color = cache.getColorHyperglycemia();
                } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                    color = cache.getColorHypoglycemia();
                }
            }
            point.setColor(color);
            point.setFillShape(true);
            point.setRadius(POINT_RADIUS);
            point.placeIn(chart);
            point.drawOn(page);
        }
    }

    private void drawTable(PdfPage page, Point position, List<Measurement> measurements) throws Exception {
        // TODO
    }
}
