package com.faltenreich.diaguard.export;

public enum ExportFormat {
    CSV,
    PDF;

    public String getExtension() {
        switch (this) {
            case CSV:
                return "csv";
            case PDF:
                return "pdf";
            default:
                return null;
        }
    }
}