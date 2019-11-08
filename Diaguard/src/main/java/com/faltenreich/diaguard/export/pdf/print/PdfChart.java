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
import com.pdfjet.Line;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class PdfChart implements PdfPrintable {

    private static final float PADDING_PARAGRAPH = 20;
    private static final float POINT_RADIUS = 5;
    private static final float CHART_LABEL_WIDTH = 28;
    private static final int X_MAX = DateTimeConstants.MINUTES_PER_DAY;
    private static final int X_STEP = 2;
    private static final int Y_STEP = 40;

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

    // TODO: Add offsets for header
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

        float labelStartY = 0f;
        int hour = 0;
        while (hour <= DateTimeConstants.HOURS_PER_DAY) {
            TextLine text = new TextLine(cache.getFontNormal());
            labelStartY = chartHeight - text.getHeight();
            // TODO
            hour += X_STEP;
        }

        Point contentStart = new Point(CHART_LABEL_WIDTH, 0);
        Point contentEnd = new Point(chartWidth, labelStartY);
        float contentWidth = contentEnd.getX() - contentStart.getX();
        float contentHeight = contentEnd.getY() - contentStart.getY();

        int labelValue = Y_STEP;
        float labelY;
        while ((labelY = contentHeight - ((labelValue / yMax) * contentHeight)) >= 0) {
            TextLine text = new TextLine(cache.getFontNormal());
            text.setText(PreferenceHelper.getInstance().getMeasurementForUi(Measurement.Category.BLOODSUGAR, labelValue));
            text.setColor(Color.gray);
            text.setPosition(0, labelY + (text.getHeight() / 4));
            text.placeIn(chart);
            text.drawOn(page);

            Line line = new Line();
            line.setStartPoint(CHART_LABEL_WIDTH, labelY);
            line.setEndPoint(chartWidth, labelY);
            line.setColor(Color.gray);
            line.placeIn(chart);
            line.drawOn(page);

            labelValue += Y_STEP;
        }

        for (BloodSugar bloodSugar : bloodSugars) {
            Entry entry = bloodSugar.getEntry();
            float minute = entry.getDate().getMinuteOfDay();
            float value = bloodSugar.getMgDl();
            float x = contentStart.getX() + ((minute / X_MAX) * contentWidth);
            float y = contentStart.getY() + (contentHeight - (value / yMax) * contentHeight);

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
