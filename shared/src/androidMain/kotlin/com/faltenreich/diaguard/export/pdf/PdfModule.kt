package com.faltenreich.diaguard.export.pdf

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun pdfModule() = module {
    single<PdfExport> { AndroidPdfExport(androidContext()) }
}