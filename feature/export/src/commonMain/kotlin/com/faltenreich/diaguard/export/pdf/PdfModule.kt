package com.faltenreich.diaguard.export.pdf

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun pdfModule() = module {
    includes(pdfPlatformModule())

    factoryOf(::ExportPdfUseCase)
}

expect fun pdfPlatformModule(): Module