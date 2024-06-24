package com.faltenreich.diaguard.shared.file

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun fileModule() = module {
    singleOf(::SystemFileReader)
    singleOf(::ResourceFileReader)
}