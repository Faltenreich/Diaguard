package com.faltenreich.diaguard.feature.export.job.pdf.print;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNote;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNoteFactory;
import com.faltenreich.diaguard.feature.export.job.pdf.view.CellBuilder;
import com.faltenreich.diaguard.feature.export.job.pdf.view.CellFactory;
import com.faltenreich.diaguard.feature.export.job.pdf.view.SizedTable;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListItem;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.pdfjet.Align;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PdfTable implements PdfPrintable {

    private static final String TAG = PdfTable.class.getSimpleName();
    private static final int HOURS_TO_SKIP = 2;

    private final PdfExportCache cache;
    private final List<Entry> entriesOfDay;
    private final List<List<Cell>> measurements;
    private final List<List<Cell>> notes;

    PdfTable(PdfExportCache cache, List<Entry> entriesOfDay) {
        this.cache = cache;
        this.entriesOfDay = entriesOfDay;
        this.measurements = new ArrayList<>();
        this.notes = new ArrayList<>();
        init();
    }

    @Override
    public void drawOn(PdfPage page) throws Exception {
        SizedTable table = new SizedTable();
        table.setData(measurements);

        float newY = page.getPosition().getY() + table.getHeight();
        float maxY = page.getEndPoint().getY();
        if (newY > maxY) {
            page = new PdfPage(cache);
            cache.setPage(page);
        }

        table.setLocation(page.getPosition().getX(), page.getPosition().getY());
        table.drawOn(page);

        table.setLocation(page.getPosition().getX(), page.getPosition().getY() + table.getHeight());
        // TODO: Chunk notes on exceeding page size
        table.setData(notes);
        table.drawOn(page);

        // TODO: Add heights of all SizedTables
        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + PdfPage.MARGIN);
    }

    private void init() {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContext();

        List<Cell> cells = new ArrayList<>();
        Cell headerCell = new CellBuilder(new Cell(cache.getFontBold()))
            .setWidth(getLabelWidth())
            .setText(DateTimeUtils.toWeekDayAndDate(cache.getDateTime()))
            .build();
        cells.add(headerCell);

        float cellWidth = (cache.getPage().getWidth() - getLabelWidth()) / (DateTimeConstants.HOURS_PER_DAY / 2f);
        for (int hour = 0; hour < DateTimeConstants.HOURS_PER_DAY; hour += PdfTable.HOURS_TO_SKIP) {
            Cell hourCell = new CellBuilder(new Cell(cache.getFontNormal()))
                .setWidth(cellWidth)
                .setText(Integer.toString(hour))
                .setForegroundColor(Color.gray)
                .setTextAlignment(Align.CENTER)
                .build();
            cells.add(hourCell);
        }
        measurements.add(cells);

        LinkedHashMap<Category, CategoryValueListItem[]> values = EntryDao.getInstance().getAverageDataTable(cache.getDateTime(), config.getCategories(), HOURS_TO_SKIP);
        int rowIndex = 0;
        for (Category category : values.keySet()) {
            CategoryValueListItem[] items = values.get(category);
            if (items != null) {
                String label = context.getString(category.getStringAcronymResId());
                int backgroundColor = rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white;
                switch (category) {
                    case INSULIN:
                        if (config.splitInsulin()) {
                            measurements.add(createMeasurementRows(cache, items, cellWidth, 0, label + " " + context.getString(R.string.bolus), backgroundColor));
                            measurements.add(createMeasurementRows(cache, items, cellWidth, 1, label + " " + context.getString(R.string.correction), backgroundColor));
                            measurements.add(createMeasurementRows(cache, items, cellWidth, 2, label + " " + context.getString(R.string.basal), backgroundColor));
                        } else {
                            measurements.add(createMeasurementRows(cache, items, cellWidth, -1, label, backgroundColor));
                        }
                        break;
                    case PRESSURE:
                        measurements.add(createMeasurementRows(cache, items, cellWidth, 0, label + " " + context.getString(R.string.systolic_acronym), backgroundColor));
                        measurements.add(createMeasurementRows(cache, items, cellWidth, 1, label + " " + context.getString(R.string.diastolic_acronym), backgroundColor));
                        break;
                    default:
                        measurements.add(createMeasurementRows(cache, items, cellWidth, 0, label, backgroundColor));
                        break;
                }
                rowIndex++;
            }
        }

        if (config.exportNotes() || config.exportTags() || config.exportFood()) {
            List<PdfNote> pdfNotes = new ArrayList<>();
            for (Entry entry : entriesOfDay) {
                PdfNote pdfNote = PdfNoteFactory.createNote(config, entry);
                if (pdfNote != null) {
                    pdfNotes.add(pdfNote);
                }
            }
            notes.addAll(CellFactory.createRowsForNotes(cache, pdfNotes, getLabelWidth()));
        }

        // FIXME: Never executed (Legacy bug)
        boolean isEmpty = measurements.isEmpty() && notes.isEmpty();
        if (isEmpty) {
            notes.add(CellFactory.createEmptyRow(cache));
        }
    }

    private List<Cell> createMeasurementRows(PdfExportCache cache, CategoryValueListItem[] items, float cellWidth, int valueIndex, String label, int backgroundColor) {
        List<Cell> cells = new ArrayList<>();

        Cell labelCell = new CellBuilder(new Cell(cache.getFontNormal()))
            .setWidth(getLabelWidth())
            .setText(label)
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(Color.gray)
            .build();
        cells.add(labelCell);

        for (CategoryValueListItem item : items) {
            Category category = item.getCategory();
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
            int textColor = Color.black;
            if (category == Category.BLOODSUGAR && cache.getConfig().highlightLimits()) {
                if (value > PreferenceStore.getInstance().getLimitHyperglycemia()) {
                    textColor = cache.getColorHyperglycemia();
                } else if (value < PreferenceStore.getInstance().getLimitHypoglycemia()) {
                    textColor = cache.getColorHypoglycemia();
                }
            }
            float customValue = PreferenceStore.getInstance().formatDefaultToCustomUnit(category, value);
            String text = customValue > 0 ? FloatUtils.parseFloat(customValue) : "";

            Cell measurementCell = new CellBuilder(new Cell(cache.getFontNormal()))
                .setWidth(cellWidth)
                .setText(text)
                .setTextAlignment(Align.CENTER)
                .setBackgroundColor(backgroundColor)
                .setForegroundColor(textColor)
                .build();
            cells.add(measurementCell);
        }
        return cells;
    }
}
