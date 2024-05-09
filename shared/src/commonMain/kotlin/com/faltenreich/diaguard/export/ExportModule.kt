package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.export.pdf.PdfExport
import com.faltenreich.diaguard.export.pdf.PdfRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun exportModule() = module {
    singleOf(::PdfExport)
    singleOf(::PdfRepository)

    singleOf(::ExportUseCase)

    factoryOf(::ExportFormViewModel)
}