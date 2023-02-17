package com.faltenreich.diaguard.feature.export.job.pdf.print;

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

public class PdfTimeline implements PdfPrintable {

    private static final int COLUMN_INDEX_NOTE = 1;
    private static final float POINT_RADIUS = 5;
    private static final float PADDING = 12;
    private static final int HOUR_INTERVAL = 2;
    private static final int HEADER_HEIGHT = 22;

    private final PdfExportCache cache;
    private final PdfCellFactory cellFactory;
    private final List<Entry> entriesOfDay;

    PdfTimeline(PdfExportCache cache, PdfCellFactory cellFactory, List<Entry> entriesOfDay) {
        this.cache = cache;
        this.cellFactory = cellFactory;
        this.entriesOfDay = entriesOfDay;
    }

    private boolean showChartForBloodSugar() {
        return cache.getConfig().hasCategory(Category.BLOODSUGAR);
    }

    @Override
    public void print() throws Exception {
        if (entriesOfDay.isEmpty()) {
            SizedTable table = new SizedTable();
            table.setData(Arrays.asList(
                Collections.singletonList(cellFactory.getDayCell()),
                cellFactory.getEmptyRow())
            );
            if (cache.getPage().getPosition().getY() + table.getHeight() > cache.getPage().getEndPoint().getY()) {
                cache.setPage(new PdfPage(cache));
            }
            table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
            table.drawOn(cache.getPage());
            cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + table.getHeight());
        } else {
            List<BloodSugar> bloodSugars = new ArrayList<>();
            List<PdfNote> notes = new ArrayList<>();

            for (Entry entry : entriesOfDay) {
                if (showChartForBloodSugar()) {
                    List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                    for (Measurement measurement : measurements) {
                        if (measurement instanceof BloodSugar) {
                            bloodSugars.add((BloodSugar) measurement);
                        }
                    }
                }
                PdfNote note = PdfNoteFactory.createNote(cache.getConfig(), entry);
                if (note != null) {
                    notes.add(note);
                }
            }

            List<Category> categories = new ArrayList<>();
            for (Category category : cache.getConfig().getCategories()) {
                if (category != Category.BLOODSUGAR) {
                    categories.add(category);
                }
            }
            LinkedHashMap<Category, CategoryValueListItem[]> measurements = EntryDao.getInstance().getAverageDataTable(
                cache.getDateTime(),
                categories.toArray(new Category[0]),
                HOUR_INTERVAL
            );

            printChart(bloodSugars);
            printTable(measurements);
            printNotes(notes);
        }

        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + PdfPage.MARGIN);
    }

    private void printChart(List<BloodSugar> bloodSugars) throws Exception {
        float width = cache.getPage().getWidth();
        SizedBox chart = new SizedBox(width, showChartForBloodSugar() ? (width / 4) : HEADER_HEIGHT);
        chart.setColor(Color.transparent);

        if (cache.getPage().getPosition().getY() + chart.getHeight() > cache.getPage().getEndPoint().getY()) {
            cache.setPage(new PdfPage(cache));
        }

        chart.setPosition(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());

        TextLine label = new TextLine(cache.getFontNormal());
        label.setColor(Color.gray);

        Line line = new Line();
        line.setColor(Color.gray);

        float chartHeight = chart.getHeight();
        float chartStartX = 0;
        float chartEndX = chartStartX + chart.getWidth();
        float chartStartY = 0;
        float chartEndY = chartStartY + chartHeight;

        float contentStartX = cellFactory.getLabelWidth();
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
        header.drawOn(cache.getPage());

        // Labels for x axis
        int minutes = 0;
        while (minutes <= xMax) {
            float x = contentStartX + ((float) minutes / xMax) * contentWidth;

            label.setText(String.valueOf(minutes / 60));
            label.setPosition(x - label.getWidth() / 2, chartStartY + header.getHeight());
            label.placeIn(chart);
            label.drawOn(cache.getPage());

            line.setStartPoint(x, chartStartY + header.getHeight() + 8);
            line.setEndPoint(x, contentEndY);
            line.placeIn(chart);
            line.drawOn(cache.getPage());

            minutes += xStep;
        }

        if (showChartForBloodSugar()) {
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
                label.drawOn(cache.getPage());

                line.setStartPoint(chartStartX + label.getWidth() + PADDING, labelY);
                line.setEndPoint(contentEndX, labelY);
                line.placeIn(chart);
                line.drawOn(cache.getPage());

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
                point.drawOn(cache.getPage());
            }
        }

        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + chart.getHeight());
    }

    private void printTable(LinkedHashMap<Category, CategoryValueListItem[]> measurements) throws Exception {
        SizedTable table = new SizedTable();
        table.setData(cellFactory.getTimelineRows(cache.getContext(), measurements, HOUR_INTERVAL));

        if (cache.getPage().getPosition().getY() + table.getHeight() > cache.getPage().getEndPoint().getY()) {
            cache.setPage(new PdfPage(cache));
        }

        table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
        table.drawOn(cache.getPage());

        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + table.getHeight());
    }

    private void printNotes(List<PdfNote> notes) throws Exception {
        for (int noteIndex = 0; noteIndex < notes.size(); noteIndex++) {
            PdfNote note = notes.get(noteIndex);
            SizedTable table = new SizedTable();
            List<Cell> row = cellFactory.getNoteRow(note, noteIndex == 0);
            float rowHeight = row.get(COLUMN_INDEX_NOTE).getHeight();
            if (cache.getPage().getPosition().getY() + rowHeight > cache.getPage().getEndPoint().getY()) {
                cache.setPage(new PdfPage(cache));
                Cell headerCell = new CellBuilder(new Cell(cache.getFontBold()))
                    .setWidth(cellFactory.getLabelWidth())
                    .setText(DateTimeUtils.toWeekDayAndDate(cache.getDateTime()))
                    .build();
                table.setData(Arrays.asList(Collections.singletonList(headerCell), row));
            } else {
                table.setData(Collections.singletonList(row));
            }
            table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
            table.drawOn(cache.getPage());
            cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + table.getHeight());
        }
    }
}
