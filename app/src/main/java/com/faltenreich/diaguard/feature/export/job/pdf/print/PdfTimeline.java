package com.faltenreich.diaguard.feature.export.job.pdf.print;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNote;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNoteFactory;
import com.faltenreich.diaguard.feature.export.job.pdf.view.CellBuilder;
import com.faltenreich.diaguard.feature.export.job.pdf.view.SizedBox;
import com.faltenreich.diaguard.feature.export.job.pdf.view.SizedTable;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListItem;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.pdfjet.Align;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Line;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PdfTimeline implements PdfPrintable {

    private static final int COLUMN_INDEX_NOTE = 1;
    private static final float POINT_RADIUS = 5;
    private static final float PADDING = 12;
    private static final int HOUR_INTERVAL = 2;
    private static final int HEADER_HEIGHT = 22;

    private final PdfExportCache cache;
    private final PdfCellFactory cellFactory;
    private final List<Entry> entriesOfDay;
    private final SizedBox chart;
    private final List<List<Cell>> tableData;
    private final List<List<Cell>> notes;

    private final boolean showChartForBloodSugar;
    private List<BloodSugar> bloodSugars;
    private LinkedHashMap<Category, CategoryValueListItem[]> measurements;
    private List<PdfNote> pdfNotes;

    PdfTimeline(PdfExportCache cache, List<Entry> entriesOfDay) {
        float width = cache.getPage().getWidth();
        this.cache = cache;
        this.cellFactory = new PdfCellFactory(cache);
        this.entriesOfDay = entriesOfDay;
        this.showChartForBloodSugar = cache.getConfig().hasCategory(Category.BLOODSUGAR);
        this.chart = new SizedBox(width, showChartForBloodSugar ? (width / 4) : HEADER_HEIGHT);
        this.tableData = new ArrayList<>();
        this.notes = new ArrayList<>();
        init();
    }

    @Override
    public void drawOn(PdfPage page) throws Exception {
        SizedTable table = new SizedTable();
        table.setData(tableData);

        if (page.getPosition().getY() + table.getHeight() + chart.getHeight() > page.getEndPoint().getY()) {
            page = new PdfPage(cache);
        }

        chart.setPosition(page.getPosition().getX(), page.getPosition().getY());
        drawChart(page, page.getPosition(), bloodSugars);
        page.getPosition().setY(page.getPosition().getY() + chart.getHeight());

        table.setLocation(page.getPosition().getX(), page.getPosition().getY());
        table.drawOn(page);
        page.getPosition().setY(page.getPosition().getY() + table.getHeight());

        for (List<Cell> row : notes) {
            float rowHeight = row.get(COLUMN_INDEX_NOTE).getHeight();
            if (page.getPosition().getY() + rowHeight > page.getEndPoint().getY()) {
                page = new PdfPage(cache);
                Cell headerCell = new CellBuilder(new Cell(cache.getFontBold()))
                    .setWidth(getLabelWidth())
                    .setText(DateTimeUtils.toWeekDayAndDate(cache.getDateTime()))
                    .build();
                rowHeight += headerCell.getHeight();
                table.setData(Arrays.asList(Collections.singletonList(headerCell), row));
            } else {
                table.setData(Collections.singletonList(row));
            }
            table.setLocation(page.getPosition().getX(), page.getPosition().getY());
            table.drawOn(page);
            page.getPosition().setY(page.getPosition().getY() + rowHeight);
        }

        // TODO: Is never true since empty rows will be created for every category
        if (tableData.isEmpty() && notes.isEmpty()) {
            List<Cell> row = cellFactory.getEmptyRow();
            float rowHeight = row.get(0).getHeight();
            table.setData(Collections.singletonList(row));
            if (page.getPosition().getY() + rowHeight > page.getEndPoint().getY()) {
                page = new PdfPage(cache);
            }
            table.setLocation(page.getPosition().getX(), page.getPosition().getY());
            table.drawOn(page);
            page.getPosition().setY(page.getPosition().getY() + rowHeight);
        }

        page.getPosition().setY(page.getPosition().getY() + PdfPage.MARGIN);

        cache.setPage(page);
    }

    private void init() {
        fetchData();
        initTable();
    }

    private void fetchData() {
        bloodSugars = new ArrayList<>();
        pdfNotes = new ArrayList<>();

        for (Entry entry : entriesOfDay) {
            if (showChartForBloodSugar) {
                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                for (Measurement measurement : measurements) {
                    if (measurement instanceof BloodSugar) {
                        bloodSugars.add((BloodSugar) measurement);
                    }
                }
            }

            PdfNote pdfNote = PdfNoteFactory.createNote(cache.getConfig(), entry);
            if (pdfNote != null) {
                pdfNotes.add(pdfNote);
            }
        }

        List<Category> categories = new ArrayList<>();
        for (Category category : cache.getConfig().getCategories()) {
            if (category != Category.BLOODSUGAR) {
                categories.add(category);
            }
        }
        measurements = EntryDao.getInstance().getAverageDataTable(
            cache.getDateTime(),
            categories.toArray(new Category[0]),
            HOUR_INTERVAL
        );
    }

    private void initTable() {
        Context context = cache.getContext();

        int rowIndex = 0;
        for (Map.Entry<Category, CategoryValueListItem[]> entry : measurements.entrySet()) {
            Category category = entry.getKey();
            CategoryValueListItem[] values = entry.getValue();
            String label = context.getString(category.getStringAcronymResId());
            switch (category) {
                case INSULIN:
                    if (cache.getConfig().splitInsulin()) {
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

        notes.addAll(cellFactory.getNoteRows(pdfNotes));
    }

    private List<Cell> createRowForMeasurements(Category category, CategoryValueListItem[] values, int rowIndex, int valueIndex, String label) {
        List<Cell> row = new ArrayList<>();

        Cell titleCell = new Cell(cache.getFontNormal());
        titleCell.setText(label);
        titleCell.setWidth(getLabelWidth());
        titleCell.setBgColor(rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white);
        titleCell.setFgColor(Color.gray);
        titleCell.setPenColor(Color.gray);
        row.add(titleCell);

        for (CategoryValueListItem item : values) {
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
            float customValue = PreferenceStore.getInstance().formatDefaultToCustomUnit(category, value);
            String text = customValue > 0 ? FloatUtils.parseFloat(customValue) : "";
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

    private Point drawChart(PdfPage page, Point position, List<BloodSugar> bloodSugars) throws Exception {
        chart.setColor(Color.transparent);
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

        if (showChartForBloodSugar) {
            float yMin = 40;
            float yMax = 210;
            for (BloodSugar bloodSugar : bloodSugars) {
                if (bloodSugar.getMgDl() > yMax) {
                    yMax = bloodSugar.getMgDl();
                }
            }
            int yStep = (int) ((yMax - yMin) / 5);
            yStep = Math.round((yStep + 10) / 10) * 10;

            // Labels for y axis
            int labelValue = yStep;
            float labelY;
            while ((labelY = contentStartY + contentHeight - ((labelValue / yMax) * contentHeight)) >= contentStartY) {
                label.setText(PreferenceStore.getInstance().getMeasurementForUi(Category.BLOODSUGAR, labelValue));
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
                if (cache.getConfig().highlightLimits()) {
                    if (value > PreferenceStore.getInstance().getLimitHyperglycemia()) {
                        color = cache.getColorHyperglycemia();
                    } else if (value < PreferenceStore.getInstance().getLimitHypoglycemia()) {
                        color = cache.getColorHypoglycemia();
                    }
                }
                point.setColor(color);
                point.placeIn(chart);
                point.drawOn(page);
            }
        }

        return new Point(position.getX(), coordinates[1]);
    }
}
