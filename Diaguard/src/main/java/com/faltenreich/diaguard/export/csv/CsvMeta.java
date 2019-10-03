package com.faltenreich.diaguard.export.csv;

public class CsvMeta {
    public static final String CSV_MIME_TYPE = "text/csv";
    public static final String CSV_IMPORT_MIME_TYPE = "text/*"; // Workaround: text/csv does not work for all apps
    public static final char CSV_DELIMITER = ';';
    public static final String CSV_KEY_META = "meta";
}
