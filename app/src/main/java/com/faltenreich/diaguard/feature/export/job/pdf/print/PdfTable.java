package com.faltenreich.diaguard.feature.export.job.pdf.print;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNote;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfNoteFactory;
import com.faltenreich.diaguard.feature.export.job.pdf.view.SizedTable;
import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListItem;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.pdfjet.Cell;
import com.pdfjet.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class PdfTable implements PdfPrintable {

    private static final int COLUMN_INDEX_NOTE = 1;
    private static final int HOURS_TO_SKIP = 2;

    private final PdfExportCache cache;
    private final PdfCellFactory cellFactory;
    private final List<Entry> entriesOfDay;

    PdfTable(PdfExportCache cache, List<Entry> entriesOfDay) {
        this.cache = cache;
        this.cellFactory = new PdfCellFactory(cache);
        this.entriesOfDay = entriesOfDay;
    }

    @Override
    public void print() throws Exception {
        drawTableOn();
        drawNotesOn();
        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + PdfPage.MARGIN);
    }

    private void drawTableOn() throws Exception {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContext();
        SizedTable table = new SizedTable();
        List<List<Cell>> tableData = new ArrayList<>();

        Cell dayCell = cellFactory.getDayCell();

        List<Cell> header = new ArrayList<>();
        header.add(dayCell);
        header.addAll(cellFactory.getHourRow(PdfTable.HOURS_TO_SKIP));
        tableData.add(header);

        if (entriesOfDay.isEmpty()) {
            tableData.add(cellFactory.getEmptyRow());
        } else {
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
                                tableData.add(cellFactory.getTableRow(items, 0, label + " " + context.getString(R.string.bolus), backgroundColor));
                                tableData.add(cellFactory.getTableRow(items,1, label + " " + context.getString(R.string.correction), backgroundColor));
                                tableData.add(cellFactory.getTableRow(items, 2, label + " " + context.getString(R.string.basal), backgroundColor));
                            } else {
                                tableData.add(cellFactory.getTableRow(items, -1, label, backgroundColor));
                            }
                            break;
                        case PRESSURE:
                            tableData.add(cellFactory.getTableRow(items, 0, label + " " + context.getString(R.string.systolic_acronym), backgroundColor));
                            tableData.add(cellFactory.getTableRow(items, 1, label + " " + context.getString(R.string.diastolic_acronym), backgroundColor));
                            break;
                        default:
                            tableData.add(cellFactory.getTableRow(items, 0, label, backgroundColor));
                            break;
                    }
                    rowIndex++;
                }
            }
        }

        table.setData(tableData);
        if (cache.getPage().getPosition().getY() + table.getHeight() > cache.getPage().getEndPoint().getY()) {
            cache.setPage(new PdfPage(cache));
        }
        table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
        table.drawOn(cache.getPage());
        cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + table.getHeight());
    }

    private void drawNotesOn() throws Exception {
        PdfExportConfig config = cache.getConfig();
        SizedTable table = new SizedTable();
        boolean isFirst = true;
        for (Entry entry : entriesOfDay) {
            PdfNote pdfNote = PdfNoteFactory.createNote(config, entry);
            if (pdfNote != null) {
                List<Cell> row = cellFactory.getNoteRow(pdfNote, isFirst);
                float rowHeight = row.get(COLUMN_INDEX_NOTE).getHeight();
                if (cache.getPage().getPosition().getY() + rowHeight > cache.getPage().getEndPoint().getY()) {
                    cache.setPage(new PdfPage(cache));
                    List<Cell> header = Collections.singletonList(cellFactory.getDayCell());
                    table.setData(Arrays.asList(header, row));
                } else {
                    table.setData(Collections.singletonList(row));
                }
                table.setLocation(cache.getPage().getPosition().getX(), cache.getPage().getPosition().getY());
                table.drawOn(cache.getPage());
                cache.getPage().getPosition().setY(cache.getPage().getPosition().getY() + table.getHeight());
                isFirst = false;
            }
        }
    }
}
