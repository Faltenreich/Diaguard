package com.faltenreich.diaguard.feature.export.job.pdf.print;

import android.content.Context;

import com.faltenreich.diaguard.R;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class PdfTable implements PdfPrintable {

    private static final int COLUMN_INDEX_NOTE = 1;
    private static final int HOURS_TO_SKIP = 2;

    private final PdfExportCache cache;
    private final CellFactory cellFactory;
    private final List<Entry> entriesOfDay;

    PdfTable(PdfExportCache cache, List<Entry> entriesOfDay) {
        this.cache = cache;
        this.cellFactory = new CellFactory(cache);
        this.entriesOfDay = entriesOfDay;
    }

    @Override
    public void drawOn(PdfPage page) throws Exception {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContext();

        SizedTable table = new SizedTable();
        List<List<Cell>> tableData = new ArrayList<>();

        Cell dayCell = cellFactory.getDayCell();

        List<Cell> header = new ArrayList<>();
        header.add(dayCell);
        header.addAll(cellFactory.getHourCells(PdfTable.HOURS_TO_SKIP));
        tableData.add(header);

        if (entriesOfDay.isEmpty()) {
            tableData.add(cellFactory.getEmptyCells());
        } else {
            LinkedHashMap<Category, CategoryValueListItem[]> values = EntryDao.getInstance().getAverageDataTable(cache.getDateTime(), config.getCategories(), HOURS_TO_SKIP);
            float cellWidth = (cache.getPage().getWidth() - getLabelWidth()) / (DateTimeConstants.HOURS_PER_DAY / 2f);
            int rowIndex = 0;
            for (Category category : values.keySet()) {
                CategoryValueListItem[] items = values.get(category);
                if (items != null) {
                    String label = context.getString(category.getStringAcronymResId());
                    int backgroundColor = rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white;
                    switch (category) {
                        case INSULIN:
                            if (config.splitInsulin()) {
                                tableData.add(createMeasurementRows(cache, items, cellWidth, 0, label + " " + context.getString(R.string.bolus), backgroundColor));
                                tableData.add(createMeasurementRows(cache, items, cellWidth, 1, label + " " + context.getString(R.string.correction), backgroundColor));
                                tableData.add(createMeasurementRows(cache, items, cellWidth, 2, label + " " + context.getString(R.string.basal), backgroundColor));
                            } else {
                                tableData.add(createMeasurementRows(cache, items, cellWidth, -1, label, backgroundColor));
                            }
                            break;
                        case PRESSURE:
                            tableData.add(createMeasurementRows(cache, items, cellWidth, 0, label + " " + context.getString(R.string.systolic_acronym), backgroundColor));
                            tableData.add(createMeasurementRows(cache, items, cellWidth, 1, label + " " + context.getString(R.string.diastolic_acronym), backgroundColor));
                            break;
                        default:
                            tableData.add(createMeasurementRows(cache, items, cellWidth, 0, label, backgroundColor));
                            break;
                    }
                    rowIndex++;
                }
            }
        }

        table.setData(tableData);

        if (page.getPosition().getY() + table.getHeight() > page.getEndPoint().getY()) {
            page = new PdfPage(cache);
        }
        table.setLocation(page.getPosition().getX(), page.getPosition().getY());
        table.drawOn(page);

        page.getPosition().setY(page.getPosition().getY() + table.getHeight());

        if (config.exportNotes() || config.exportTags() || config.exportFood()) {
            boolean isFirst = true;
            for (Entry entry : entriesOfDay) {
                PdfNote pdfNote = PdfNoteFactory.createNote(config, entry);
                if (pdfNote != null) {
                    List<Cell> row = cellFactory.createRowForNote(pdfNote, isFirst);
                    float rowHeight = row.get(COLUMN_INDEX_NOTE).getHeight();
                    if (page.getPosition().getY() + rowHeight > page.getEndPoint().getY()) {
                        page = new PdfPage(cache);
                        rowHeight += dayCell.getHeight();
                        table.setData(Arrays.asList(Collections.singletonList(dayCell), row));
                    } else {
                        table.setData(Collections.singletonList(row));
                    }
                    table.setLocation(page.getPosition().getX(), page.getPosition().getY());
                    table.drawOn(page);
                    page.getPosition().setY(page.getPosition().getY() + rowHeight);
                    isFirst = false;
                }
            }
        }

        page.getPosition().setY(page.getPosition().getY() + PdfPage.MARGIN);

        cache.setPage(page);
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
