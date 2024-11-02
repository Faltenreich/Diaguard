package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.export.pdf.pdfModule
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun exportModule() = module {
    includes(pdfModule())

    singleOf(::ExportUseCase)

    viewModelOf(::ExportFormViewModel)
}