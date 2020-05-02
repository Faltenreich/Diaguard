package com.faltenreich.diaguard.feature.export.job.pdf.meta;

import androidx.annotation.Nullable;

public enum PdfExportStyle {
    TABLE(0),
    TIMELINE(1),
    LOG(2);

    public int stableId;

    PdfExportStyle(int stableId) {
        this.stableId = stableId;
    }

    @Nullable
    public static PdfExportStyle valueOf(int stableId) {
        for (PdfExportStyle style : PdfExportStyle.values()) {
            if (style.stableId == stableId) {
                return style;
            }
        }
        return null;
    }
}