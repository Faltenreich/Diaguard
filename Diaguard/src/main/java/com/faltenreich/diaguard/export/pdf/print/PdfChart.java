package com.faltenreich.diaguard.export.pdf.print;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.view.SizedBox;
import com.faltenreich.diaguard.export.pdf.view.SizedTable;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.pdfjet.Color;
import com.pdfjet.Line;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class PdfChart implements PdfPrintable {

    private static final float PADDING_CONTENT = 12;
    private static final float PADDING_PARAGRAPH = 20;
    private static final float POINT_RADIUS = 5;
    private static final float CHART_LABEL_WIDTH = 28;

    private PdfExportCache cache;
    private TextLine header;
    private SizedBox chart;
    private SizedTable table;

    public PdfChart(PdfExportCache cache, float width) {
        this.cache = cache;
        this.header = new TextLine(cache.getFontBold());
        this.chart = new SizedBox(width, width / 3);
        this.table = new SizedTable();
    }

    @Override
    public float getHeight() {
        return header.getHeight() +
            PADDING_CONTENT +
            chart.getHeight() +
            PADDING_CONTENT +
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

        drawHeader(page, position);
        drawChart(page, position, bloodSugars);
        drawTable(page, position, otherMeasurements);
    }

    private void drawHeader(PdfPage page, Point position) throws Exception {
        header.setText(DateTimeUtils.toWeekDayAndDate(cache.getDateTime()));
        header.setPosition(position.getX(), position.getY());
        float[] newPosition = header.drawOn(page);
        position.setY(newPosition[1] + PADDING_CONTENT);
    }

    private void drawChart(PdfPage page, Point position, List<BloodSugar> bloodSugars) throws Exception {
        chart.setColor(Color.transparent);
        chart.setPosition(position.getX(), position.getY());
        float[] newPosition = chart.drawOn(page);
        position.setY(newPosition[1] + PADDING_CONTENT);

        TextLine label = new TextLine(cache.getFontNormal());
        label.setColor(Color.gray);

        Line line = new Line();
        line.setColor(Color.gray);

        float chartWidth = chart.getWidth();
        float chartHeight = chart.getHeight();
        float chartStartX = 0;
        float chartEndX = chartStartX + chart.getWidth();
        float chartStartY = 0; // TODO: Add offset of header
        float chartEndY = chartStartY + chartHeight;

        float contentStartX = CHART_LABEL_WIDTH;
        float contentStartY = chartStartY;
        float contentEndX = chartEndX;
        float contentEndY = contentStartY + chartEndY - label.getHeight();
        float contentWidth = contentEndX - contentStartX;
        float contentHeight = contentEndY - contentStartY;

        int xStep = DateTimeConstants.MINUTES_PER_HOUR * 2;
        float xMax = DateTimeConstants.MINUTES_PER_DAY;
        int yStep = 40;
        float yMax = 250;
        for (BloodSugar bloodSugar : bloodSugars) {
            if (bloodSugar.getMgDl() > yMax) {
                yMax = bloodSugar.getMgDl();
            }
        }

        // Labels for x axis
        int minutes = 0;
        while (minutes <= xMax) {
            float x = contentStartX + ((float) minutes / xMax) * contentWidth;

            label.setText(String.valueOf(minutes / 60));
            label.setPosition(x - label.getWidth() / 2, chartEndY);
            label.placeIn(chart);
            label.drawOn(page);

            line.setStartPoint(x, contentStartY);
            line.setEndPoint(x, contentEndY);
            line.placeIn(chart);
            line.drawOn(page);

            minutes += xStep;
        }

        // Labels for y axis
        int labelValue = 0;
        float labelY;
        while ((labelY = contentStartY + contentHeight - ((labelValue / yMax) * contentHeight)) >= 0) {
            // Skip first label
            if (labelValue > 0) {
                label.setText(PreferenceHelper.getInstance().getMeasurementForUi(Measurement.Category.BLOODSUGAR, labelValue));
                label.setPosition(chartStartX, labelY + (label.getHeight() / 4));
                label.placeIn(chart);
                label.drawOn(page);
            }

            line.setStartPoint(CHART_LABEL_WIDTH, labelY);
            line.setEndPoint(contentEndX, labelY);
            line.placeIn(chart);
            line.drawOn(page);

            labelValue += yStep;
        }

        Point point = new Point();
        point.setFillShape(true);
        point.setRadius(POINT_RADIUS);
        for (BloodSugar bloodSugar : bloodSugars) {
            Entry entry = bloodSugar.getEntry();
            float minute = entry.getDate().getMinuteOfDay();
            float value = bloodSugar.getMgDl();
            float x = contentStartX + ((minute / xMax) * contentWidth);
            float y = contentStartY + (contentHeight - (value / yMax) * contentHeight);

            point.setPosition(x, y);
            int color = Color.black;
            if (cache.getConfig().isHighlightLimits()) {
                if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                    color = cache.getColorHyperglycemia();
                } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                    color = cache.getColorHypoglycemia();
                }
            }
            point.setColor(color);
            point.placeIn(chart);
            point.drawOn(page);
        }
    }

    private void drawTable(PdfPage page, Point position, List<Measurement> measurements) throws Exception {
        // TODO
    }
}
