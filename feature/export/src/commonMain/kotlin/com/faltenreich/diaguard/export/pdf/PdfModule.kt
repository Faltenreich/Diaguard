package com.faltenreich.diaguard.export.pdf

import org.koin.core.module.Module
import org.koin.dsl.module

fun pdfModule() = module {
    includes(pdfPlatformModule())
}

expect fun pdfPlatformModule(): Module