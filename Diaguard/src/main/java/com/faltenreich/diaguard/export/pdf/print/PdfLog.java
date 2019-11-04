package com.faltenreich.diaguard.export.pdf.print;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.export.pdf.view.DayCellPdfView;
import com.faltenreich.diaguard.export.pdf.view.PdfCellView;
import com.faltenreich.diaguard.export.pdf.view.SizedTablePdfView;
import com.faltenreich.diaguard.util.StringUtils;
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

            int rowIndex = 0;
            for (Entry entry : entries) {
                int backgroundColor = rowIndex % 2 == 0 ? cache.getColorDivider() : Color.white;
                int oldSize = data.size();
                String time = entry.getDate().toString("HH:mm");

                for (Measurement measurement : entry.getMeasurementCache()) {
                    Measurement.Category category = measurement.getCategory();
                    data.add(getRow(
                        cache,
                        data.size() == oldSize ? time : null,
                        category.toLocalizedString(context),
                        measurement.print(),
                        backgroundColor
                    ));
                }

                List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                if (!entryTags.isEmpty()) {
                    List<String> tagNames = new ArrayList<>();
                    for (EntryTag entryTag : entryTags) {
                        String tagName = entryTag.getTag().getName();
                        if (!StringUtils.isBlank(tagName)) {
                            tagNames.add(entryTag.getTag().getName());
                        }
                    }
                    data.add(getRow(
                        cache,
                        data.size() == oldSize ? time : null,
                        context.getString(R.string.tags),
                        TextUtils.join(", ", tagNames),
                        backgroundColor
                    ));
                }

                if (!StringUtils.isBlank(entry.getNote())) {
                    data.add(getRow(
                        cache,
                        data.size() == oldSize ? time : null,
                        context.getString(R.string.note),
                        entry.getNote(),
                        backgroundColor
                    ));
                }

                rowIndex++;
            }
        }

        try {
            table.setData(data);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    // FIXME: Break lines accordingly
    private List<Cell> getRow(PdfExportCache cache, String title, String subtitle, String description, int backgroundColor) {
        List<Cell> entryRow = new ArrayList<>();

        Cell titleCell = new PdfCellView(cache.getFontNormal(), PdfLog.TIME_WIDTH);
        titleCell.setText(title);
        titleCell.setBgColor(backgroundColor);
        titleCell.setFgColor(Color.gray);
        entryRow.add(titleCell);

        Cell subtitleCell = new PdfCellView(cache.getFontNormal(), PdfLog.LABEL_WIDTH);
        subtitleCell.setText(subtitle);
        subtitleCell.setBgColor(backgroundColor);
        subtitleCell.setFgColor(Color.gray);
        entryRow.add(subtitleCell);

        Cell descriptionCell = new PdfCellView(
            cache.getFontNormal(),
            width - titleCell.getWidth() - subtitleCell.getWidth()
        );
        descriptionCell.setText(description);
        descriptionCell.setBgColor(backgroundColor);
        entryRow.add(descriptionCell);

        return entryRow;
    }
}
