package com.faltenreich.diaguard.export

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun exportModule() = module {
    singleOf(::PdfExport)
}