package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.config.BuildConfig
import com.faltenreich.diaguard.data.export.ExportSettings
import org.koin.dsl.module

actual fun pdfPlatformModule() = module {
    factory<PdfExport> {
        if (get<BuildConfig>().hasPlatformFramework()) AndroidPdfExport(context = get())
        else object : PdfExport {
            override fun export(settings: ExportSettings) = Unit
        }
    }
}