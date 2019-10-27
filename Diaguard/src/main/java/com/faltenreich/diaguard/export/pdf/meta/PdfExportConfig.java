package com.faltenreich.diaguard.export.pdf.meta;

import android.content.Context;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.ExportCallback;
import com.faltenreich.diaguard.export.ExportConfig;

import org.joda.time.DateTime;

import java.lang.ref.WeakReference;

public class PdfExportConfig extends ExportConfig {

    public static final String MIME_TYPE = "application/pdf";

    private final WeakReference<Context> contextReference;
    private final PdfExportStyle style;
    private final boolean exportNotes;
    private final boolean exportTags;
    private final boolean exportFood;
    private final boolean splitInsulin;
    private final boolean highlightLimits;

    public PdfExportConfig(
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Measurement.Category[] categories,
        Context context,
        PdfExportStyle style,
        boolean exportNotes,
        boolean exportTags,
        boolean exportFood,
        boolean splitInsulin,
        boolean highlightLimits
    ) {
        super(callback, dateStart, dateEnd, categories);
        this.contextReference = new WeakReference<>(context);
        this.style = style;
        this.exportNotes = exportNotes;
        this.exportTags = exportTags;
        this.exportFood = exportFood;
        this.splitInsulin = splitInsulin;
        this.highlightLimits = highlightLimits;
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

    public boolean isHighlightLimits() {
        return highlightLimits;
    }

    @Override
    public void persistInSharedPreferences() {
        super.persistInSharedPreferences();
        PreferenceHelper.getInstance().setPdfExportStyle(style);
        PreferenceHelper.getInstance().setExportNotes(exportNotes);
        PreferenceHelper.getInstance().setExportTags(exportTags);
        PreferenceHelper.getInstance().setExportFood(exportFood);
        PreferenceHelper.getInstance().setExportInsulinSplit(splitInsulin);
        PreferenceHelper.getInstance().setLimitsAreHighlighted(highlightLimits);
    }
}
