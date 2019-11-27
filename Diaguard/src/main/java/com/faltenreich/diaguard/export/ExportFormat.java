package com.faltenreich.diaguard.export;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;

import java.io.File;

public enum ExportFormat {
    CSV("csv", R.color.green_dark),
    PDF("pdf", R.color.red_dark);

    public String extension;
    @ColorRes public int colorRes;

    ExportFormat(String extension, @ColorRes int colorRes) {
        this.extension = extension;
        this.colorRes = colorRes;
    }

    @Nullable
    public static ExportFormat valueOf(File file) {
        for (ExportFormat format : ExportFormat.values()) {
            if (file.getName().endsWith(format.extension)) {
                return format;
            }
        }
        return null;
    }
}