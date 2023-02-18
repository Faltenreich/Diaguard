package com.faltenreich.diaguard.feature.export.job.pdf.print;

import com.faltenreich.diaguard.shared.data.database.entity.Entry;

import java.util.List;

public interface PdfPrintable {

    void print(List<Entry> entriesOfDay) throws Exception;
}
