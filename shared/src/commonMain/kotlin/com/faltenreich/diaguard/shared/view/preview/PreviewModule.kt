package com.faltenreich.diaguard.shared.view.preview

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun previewModule() = module {
    factoryOf(::PreviewScope)
}