package com.faltenreich.diaguard.feature.export.job;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;

import java.io.File;

public enum FileType {
    PDF(FileType.MIME_TYPE_PDF, "pdf", R.color.red_dark),
    CSV(FileType.MIME_TYPE_CSV, "csv", R.color.green_dark);

    public static final String MIME_TYPE_WILDCARD = "text/*";
    public static final String MIME_TYPE_PDF = "application/pdf";
    public static final String MIME_TYPE_CSV = "text/csv";

    public String mimeType;
    public String extension;
    @ColorRes public int colorRes;

    FileType(String mimeType, String extension, @ColorRes int colorRes) {
        this.mimeType = mimeType;
        this.extension = extension;
        this.colorRes = colorRes;
    }

    @Nullable
    public static FileType valueOf(File file) {
        for (FileType format : FileType.values()) {
            if (file.getName().endsWith(format.extension)) {
                return format;
            }
        }
        return null;
    }

    public static String mimeTypeOf(File file) {
        FileType fileType = valueOf(file);
        return fileType != null ? fileType.mimeType : MIME_TYPE_WILDCARD;
    }
}