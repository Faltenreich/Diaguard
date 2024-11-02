package com.faltenreich.diaguard.export.pdf

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun pdfModule() = module {
    singleOf(::PdfExport)
    singleOf(::PdfRepository)
}