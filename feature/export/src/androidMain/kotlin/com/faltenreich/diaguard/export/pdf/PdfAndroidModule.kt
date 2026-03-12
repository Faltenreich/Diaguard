package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.config.BuildConfig
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.persistence.file.File
import org.koin.dsl.module

actual fun pdfPlatformModule() = module {
    factory<PdfExport> {
        if (get<BuildConfig>().hasPlatformFramework()) {
            AndroidPdfExport(
                context = get(),
                dateTimeFactory = get(),
                dateTimeFormatter = get(),
            )
        } else {
            object : PdfExport {
                override suspend fun export(entries: List<Entry>, settings: ExportSettings): File? =
                    null
            }
        }
    }
}