package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.config.BuildConfig
import org.koin.dsl.module

actual fun pdfModule() = module {
    factory<PdfExport> {
        if (get<BuildConfig>().hasPlatformFramework()) AndroidPdfExport(context = get())
        else PdfExport {}
    }
}