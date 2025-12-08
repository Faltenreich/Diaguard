package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.dataModule
import com.faltenreich.diaguard.export.form.ExportFormViewModel
import com.faltenreich.diaguard.export.pdf.pdfModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun exportModule() = module {
    includes(dataModule())
    includes(pdfModule())

    factoryOf(::ExportUseCase)

    viewModelOf(::ExportFormViewModel)
}