package com.faltenreich.diaguard.export.pdf

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun pdfModule() = module {
    factoryOf(::AndroidPdfExport) bind PdfExport::class
}