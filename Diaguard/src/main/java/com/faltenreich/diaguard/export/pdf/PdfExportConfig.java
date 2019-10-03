package com.faltenreich.diaguard.export.pdf;

import android.content.Context;

import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.ExportCallback;
import com.faltenreich.diaguard.export.ExportConfig;

import org.joda.time.DateTime;

import java.lang.ref.WeakReference;

public class PdfExportConfig extends ExportConfig {

    private final WeakReference<Context> contextReference;
    private final PdfExportStyle style;
    private final boolean exportNotes;
    private final boolean exportTags;
    private final boolean exportFood;
    private final boolean splitInsulin;

    public PdfExportConfig(
        Context context,
        ExportCallback callback,
        PdfExportStyle style,
        DateTime dateStart,
        DateTime dateEnd,
        Measurement.Category[] categories,
        boolean exportNotes,
        boolean exportTags,
        boolean exportFood,
        boolean splitInsulin
    ) {
        super(callback, dateStart, dateEnd, categories);
        this.contextReference = new WeakReference<>(context);
        this.style = style;
        this.exportNotes = exportNotes;
        this.exportTags = exportTags;
        this.exportFood = exportFood;
        this.splitInsulin = splitInsulin;
    }

    public  WeakReference<Context> getContextReference() {
        return contextReference;
    }

    public PdfExportStyle getStyle() {
        return style;
    }

    public boolean isExportNotes() {
        return exportNotes;
    }

    public boolean isExportTags() {
        return exportTags;
    }

    public boolean isExportFood() {
        return exportFood;
    }

    public boolean isSplitInsulin() {
        return splitInsulin;
    }
}
