package com.faltenreich.diaguard.export;

import android.content.Context;

import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;

import java.lang.ref.WeakReference;

public class ExportConfig {

    private final WeakReference<Context> contextReference;
    private final DateTime dateStart;
    private final DateTime dateEnd;
    private final Measurement.Category[] categories;
    private final boolean exportNotes;
    private final boolean exportTags;
    private final boolean exportFood;
    private final boolean splitInsulin;

    private ExportConfig(
        WeakReference<Context> contextReference,
        DateTime dateStart,
        DateTime dateEnd,
        Measurement.Category[] categories,
        boolean exportNotes,
        boolean exportTags,
        boolean exportFood,
        boolean splitInsulin
    ) {
        this.contextReference = contextReference;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.categories = categories;
        this.exportNotes = exportNotes;
        this.exportTags = exportTags;
        this.exportFood = exportFood;
        this.splitInsulin = splitInsulin;
    }

    public  WeakReference<Context> getContextReference() {
        return contextReference;
    }

    public DateTime getDateStart() {
        return dateStart;
    }

    public DateTime getDateEnd() {
        return dateEnd;
    }

    public Measurement.Category[] getCategories() {
        return categories;
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

    public static class Builder {

        private final WeakReference<Context> contextReference;
        private DateTime dateStart;
        private DateTime dateEnd;
        private Measurement.Category[] categories;
        private boolean exportNotes;
        private boolean exportTags;
        private boolean exportFood;
        private boolean splitInsulin;

        public Builder(Context context) {
            this.contextReference = new WeakReference<>(context);
        }

        public ExportConfig build() {
            return new ExportConfig(
                contextReference,
                dateStart,
                dateEnd,
                categories,
                exportNotes,
                exportTags,
                exportFood,
                splitInsulin
            );
        }

        public Builder setDateStart(DateTime dateStart) {
            this.dateStart = dateStart;
            return this;
        }

        public Builder setDateEnd(DateTime dateEnd) {
            this.dateEnd = dateEnd;
            return this;
        }

        public Builder setCategories(Measurement.Category[] categories) {
            this.categories = categories;
            return this;
        }

        public Builder setExportNotes(boolean exportNotes) {
            this.exportNotes = exportNotes;
            return this;
        }

        public Builder setExportTags(boolean exportTags) {
            this.exportTags = exportTags;
            return this;
        }

        public Builder setExportFood(boolean exportFood) {
            this.exportFood = exportFood;
            return this;
        }

        public Builder setSplitInsulin(boolean splitInsulin) {
            this.splitInsulin = splitInsulin;
            return this;
        }
    }

    public enum Style {
        TABLE,
        TIMELINE,
        LOG
    }
}
