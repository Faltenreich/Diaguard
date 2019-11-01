package com.faltenreich.diaguard.export.pdf.print;

import android.content.Context;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.export.pdf.view.DayCellPdfView;
import com.faltenreich.diaguard.export.pdf.view.PdfCellView;
import com.faltenreich.diaguard.export.pdf.view.SizedTablePdfView;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.Point;

import java.util.ArrayList;
import java.util.List;

public class PdfLog implements PdfPrintable {

    private static final String TAG = PdfLog.class.getSimpleName();

    private static final float PADDING_PARAGRAPH = 20;
    private static final float LABEL_WIDTH = 112;
    private static final float TIME_WIDTH = 72;

    private SizedTablePdfView table;
    private float width;

    public PdfLog(PdfExportCache cache, float width) {
        this.table = new SizedTablePdfView();
        this.width = width;
        init(cache);
    }

    @Override
    public float getHeight() {
        float height = table.getHeight();
        return height > 0 ? height + PADDING_PARAGRAPH : 0f;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        table.setLocation(position.getX(), position.getY());
        table.drawOn(page);
    }

    private void init(PdfExportCache cache) {
        PdfExportConfig config = cache.getConfig();
        Context context = config.getContextReference().get();

        List<List<Cell>> data = new ArrayList<>();
        List<Cell> headerRow = new ArrayList<>();
        headerRow.add(new DayCellPdfView(cache.getFontBold(), cache.getDateTime()));
        data.add(headerRow);

        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(cache.getDateTime());
        if (entries.isEmpty()) {
            Cell emptyCell = new PdfCellView(cache.getFontNormal(), width);
            emptyCell.setText(context.getString(R.string.no_data));
            emptyCell.setBgColor(cache.getColorDivider());
            emptyCell.setFgColor(Color.gray);
            List<Cell> row = new ArrayList<>();
            row.add(emptyCell);
            data.add(row);
        } else {
            for (Entry entry : entries) {
                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                entry.setMeasurementCache(measurements);
            }

            int row = 0;
            for (Entry entry : entries) {
                int backgroundColor = row % 2 == 0 ? cache.getColorDivider() : Color.white;

                List<Measurement> measurements = entry.getMeasurementCache();
                for (int measurementIndex = 0; measurementIndex < measurements.size(); measurementIndex++) {
                    Measurement measurement = measurements.get(measurementIndex);
                    Measurement.Category category = measurement.getCategory();

                    List<Cell> entryRow = new ArrayList<>();

                    boolean showTime = measurementIndex == 0;
                    Cell timeCell = new PdfCellView(cache.getFontNormal(), PdfLog.TIME_WIDTH);
                    timeCell.setText(showTime ? entry.getDate().toString("HH:mm") : null);
                    timeCell.setBgColor(backgroundColor);
                    timeCell.setFgColor(Color.gray);
                    entryRow.add(timeCell);

                    Cell categoryCell = new Cell(cache.getFontNormal());
                    categoryCell.setText(category.toLocalizedString(context));
                    categoryCell.setBgColor(backgroundColor);
                    categoryCell.setFgColor(Color.gray);
                    // TODO: Adjust width for all languages
                    categoryCell.setWidth(PdfLog.LABEL_WIDTH);
                    categoryCell.setNoBorders();
                    entryRow.add(categoryCell);

                    Cell measurementCell = new PdfCellView(cache.getFontNormal(), width - timeCell.getWidth() - categoryCell.getWidth());
                    measurementCell.setText(measurement.print());
                    measurementCell.setBgColor(backgroundColor);
                    entryRow.add(measurementCell);

                    data.add(entryRow);
                }
                row++;
            }
        }

        try {
            table.setData(data);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }
}
