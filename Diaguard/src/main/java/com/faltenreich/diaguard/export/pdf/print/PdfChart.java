package com.faltenreich.diaguard.export.pdf.print;

import android.content.Context;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.view.SizedBox;
import com.faltenreich.diaguard.export.pdf.view.SizedTable;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.Helper;
import com.pdfjet.Align;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Line;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PdfChart implements PdfPrintable {

    private static final String TAG = PdfChart.class.getSimpleName();
    private static final float POINT_RADIUS = 5;
    private static final float PADDING = 12;
    private static final int HOUR_INTERVAL = 2;

    private PdfExportCache cache;
    private SizedBox chart;
    private SizedTable table;

    private boolean showChartForBloodSugar;
    private List<BloodSugar> bloodSugars;
    private LinkedHashMap<Measurement.Category, ListItemCategoryValue[]> measurements;

    PdfChart(PdfExportCache cache) {
        float width = cache.getPage().getWidth();
        this.cache = cache;
        this.showChartForBloodSugar = cache.getConfig().hasCategory(Measurement.Category.BLOODSUGAR);
        this.chart = showChartForBloodSugar ? new SizedBox(width, width / 4) : null;
        this.table = new SizedTable();
        init();
    }

    @Override
    public float getHeight() {
        return chart != null ? chart.getHeight() : 0 + table.getHeight() + PdfPage.MARGIN;
    }

    private void init() {
        fetchData();
        initTable();
    }

    private void fetchData() {
        DateTime dateTime = cache.getDateTime();

        if (showChartForBloodSugar) {
            List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(dateTime);
            bloodSugars = new ArrayList<>();
            for (Entry entry : entries) {
                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                for (Measurement measurement : measurements) {
                    if (measurement instanceof BloodSugar) {
                        bloodSugars.add((BloodSugar) measurement);
                    }
                }
            }
        }

        List<Measurement.Category> categories = new ArrayList<>();
        for (Measurement.Category category : cache.getConfig().getCategories()) {
            if (category != Measurement.Category.BLOODSUGAR) {
                categories.add(category);
            }
        }
        measurements = EntryDao.getInstance().getAverageDataTable(
            dateTime,
            categories.toArray(new Measurement.Category[0]),
            HOUR_INTERVAL
        );
    }

    private void initTable() {
        List<List<Cell>> tableData = new ArrayList<>();
        Context context = cache.getContext();

        int rowIndex = 0;
        for (Map.Entry<Measurement.Category, ListItemCategoryValue[]> entry : measurements.entrySet()) {
            Measurement.Category category = entry.getKey();
            ListItemCategoryValue[] values = entry.getValue();
            String label = context.getString(category.getStringAcronymResId());
            switch (category) {
                case INSULIN:
                    if (cache.getConfig().isSplitInsulin()) {
                        tableData.add(createRowForMeasurements(category, values, rowIndex, 0, label + " " + context.getString(R.string.bolus)));
                        tableData.add(createRowForMeasurements(category, values, rowIndex, 1, label + " " + context.getString(R.string.correction)));
                        tableData.add(createRowForMeasurements(category, values, rowIndex, 2, label + " " + context.getString(R.string.basal)));
                    } else {
                        tableData.add(createRowForMeasurements(category, values, rowIndex, -1, label));
                    }
                    break;
                case PRESSURE:
                    tableData.add(createRowForMeasurements(category, values, rowIndex, 0,  label + " " + context.getString(R.string.systolic_acronym)));
                    tableData.add(createRowForMeasurements(category, values, rowIndex, 1, label + " " + context.getString(R.string.diastolic_acronym)));
                    break;
                default:
                    tableData.add(createRowForMeasurements(category, values, rowIndex, -1, label));
            }
            rowIndex++;
        }

        try {
            // Must be executed early to know the table's height
            table.setData(tableData);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    private List<Cell> createRowForMeasurements(Measurement.Category category, ListItemCategoryValue[] values, int rowIndex, int valueIndex, String label) {
        List<Cell> row = new ArrayList<>();

        Cell titleCell = new Cell(cache.getFontNormal());
        titleCell.setText(label);
        titleCell.setWidth(getLabelWidth());
        titleCell.setBgColor(rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white);
        titleCell.setFgColor(Color.gray);
        titleCell.setPenColor(Color.gray);
        row.add(titleCell);

        for (ListItemCategoryValue item : values) {
            Cell valueCell = new Cell(cache.getFontNormal());

            float value = 0;
            switch (valueIndex) {
                case -1:
                    value = item.getValueTotal();
                    break;
                case 0:
                    value = item.getValueOne();
                    break;
                case 1:
                    value = item.getValueTwo();
                    break;
                case 2:
                    value = item.getValueThree();
                    break;
            }
            float customValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, value);
            String text = customValue > 0 ? Helper.parseFloat(customValue) : "";
            valueCell.setText(text);

            valueCell.setWidth((cache.getPage().getWidth() - getLabelWidth()) / (DateTimeConstants.HOURS_PER_DAY / HOUR_INTERVAL));
            valueCell.setBgColor(rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white);
            valueCell.setFgColor(Color.black);
            valueCell.setPenColor(Color.gray);
            valueCell.setTextAlignment(Align.CENTER);
            row.add(valueCell);
        }
        return row;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        if (showChartForBloodSugar) {
            position = drawChart(page, position, bloodSugars);
        }
        table.setLocation(position.getX(), position.getY());
        table.drawOn(page);
    }

    private Point drawChart(PdfPage page, Point position, List<BloodSugar> bloodSugars) throws Exception {
        chart.setColor(Color.transparent);
        chart.setPosition(position.getX(), position.getY());
        float[] coordinates = chart.drawOn(page);

        TextLine label = new TextLine(cache.getFontNormal());
        label.setColor(Color.gray);

        Line line = new Line();
        line.setColor(Color.gray);

        float chartWidth = chart.getWidth();
        float chartHeight = chart.getHeight();
        float chartStartX = 0;
        float chartEndX = chartStartX + chart.getWidth();
        float chartStartY = 0;
        float chartEndY = chartStartY + chartHeight;

        float contentStartX = getLabelWidth();
        float contentStartY = chartStartY + label.getHeight() + PADDING;
        float contentEndX = chartEndX;
        float contentEndY = chartEndY;
        float contentWidth = contentEndX - contentStartX;
        float contentHeight = contentEndY - contentStartY;

        int xStep = DateTimeConstants.MINUTES_PER_HOUR * HOUR_INTERVAL;
        float xMax = DateTimeConstants.MINUTES_PER_DAY;
        float yMin = 40;
        float yMax = 210;
        for (BloodSugar bloodSugar : bloodSugars) {
            if (bloodSugar.getMgDl() > yMax) {
                yMax = bloodSugar.getMgDl();
            }
        }
        int yStep = (int) ((yMax - yMin) / 5);
        yStep = Math.round((yStep + 10) / 10) * 10;

        TextLine header = new TextLine(cache.getFontBold());
        header.setText(DateTimeUtils.toWeekDayAndDate(cache.getDateTime()));
        header.setPosition(chartStartX, chartStartY + header.getHeight());
        header.placeIn(chart);
        header.drawOn(page);

        // Labels for x axis
        int minutes = 0;
        while (minutes <= xMax) {
            float x = contentStartX + ((float) minutes / xMax) * contentWidth;

            label.setText(String.valueOf(minutes / 60));
            label.setPosition(x - label.getWidth() / 2, chartStartY + header.getHeight());
            label.placeIn(chart);
            label.drawOn(page);

            line.setStartPoint(x, chartStartY + header.getHeight() + 8);
            line.setEndPoint(x, contentEndY);
            line.placeIn(chart);
            line.drawOn(page);

            minutes += xStep;
        }

        // Labels for y axis
        int labelValue = yStep;
        float labelY;
        while ((labelY = contentStartY + contentHeight - ((labelValue / yMax) * contentHeight)) >= contentStartY) {
            label.setText(PreferenceHelper.getInstance().getMeasurementForUi(Measurement.Category.BLOODSUGAR, labelValue));
            label.setPosition(chartStartX, labelY + (label.getHeight() / 4));
            label.placeIn(chart);
            label.drawOn(page);

            line.setStartPoint(chartStartX + label.getWidth() + PADDING, labelY);
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

        return new Point(position.getX(), coordinates[1]);
    }
}
