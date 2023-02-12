package com.faltenreich.diaguard.feature.export.job.pdf.meta;

import android.content.Context;

import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.ExportConfig;
import com.faltenreich.diaguard.feature.export.job.FileType;

import org.joda.time.DateTime;

public class PdfExportConfig extends ExportConfig {

    private final PdfExportStyle style;
    private final boolean includeCalendarWeek;
    private final boolean includeGeneratedDate;
    private final boolean includePageNumber;
    private final boolean exportNotes;
    private final boolean exportTags;
    private final boolean skipEmptyDays;
    private final boolean exportFood;
    private final boolean splitInsulin;
    private final boolean highlightLimits;

    public PdfExportConfig(
        Context context,
        ExportCallback callback,
        DateTime dateStart,
        DateTime dateEnd,
        Category[] categories,
        PdfExportStyle style,
        boolean includeCalendarWeek,
        boolean includeGeneratedDate,
        boolean includePageNumber,
        boolean exportNotes,
        boolean exportTags,
        boolean skipEmptyDays,
        boolean exportFood,
        boolean splitInsulin,
        boolean highlightLimits
    ) {
        super(context, callback, dateStart, dateEnd, categories, FileType.PDF);
        this.style = style;
        this.includeCalendarWeek = includeCalendarWeek;
        this.includeGeneratedDate = includeGeneratedDate;
        this.includePageNumber = includePageNumber;
        this.exportNotes = exportNotes;
        this.exportTags = exportTags;
        this.skipEmptyDays = skipEmptyDays;
        this.exportFood = exportFood;
        this.splitInsulin = splitInsulin;
        this.highlightLimits = highlightLimits;
    }

    public PdfExportStyle getStyle() {
        return style;
    }

    public boolean includeCalendarWeek() {
        return includeCalendarWeek;
    }

    public boolean includeGeneratedDate() {
        return includeGeneratedDate;
    }

    public boolean includePageNumber() {
        return includePageNumber;
    }

    public boolean exportNotes() {
        return exportNotes;
    }

    public boolean exportTags() {
        return exportTags;
    }

    public boolean skipEmptyDays() {
        return skipEmptyDays;
    }

    public boolean exportFood() {
        return exportFood;
    }

    public boolean splitInsulin() {
        return splitInsulin;
    }

    public boolean highlightLimits() {
        return highlightLimits;
    }

    @Override
    public void persistInSharedPreferences() {
        super.persistInSharedPreferences();
        PreferenceStore.getInstance().setPdfExportStyle(style);
        PreferenceStore.getInstance().setIncludeCalendarWeek(includeCalendarWeek);
        PreferenceStore.getInstance().setIncludeGeneratedDate(includeGeneratedDate);
        PreferenceStore.getInstance().setIncludePageNumber(includePageNumber);
        PreferenceStore.getInstance().setExportNotes(exportNotes);
        PreferenceStore.getInstance().setExportTags(exportTags);
        PreferenceStore.getInstance().setSkipEmptyDays(skipEmptyDays);
        PreferenceStore.getInstance().setExportFood(exportFood);
        PreferenceStore.getInstance().setExportInsulinSplit(splitInsulin);
        PreferenceStore.getInstance().setLimitsAreHighlighted(highlightLimits);
    }
}
