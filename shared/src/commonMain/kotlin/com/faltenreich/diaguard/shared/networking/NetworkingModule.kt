package com.faltenreich.diaguard.shared.networking

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun networkingModule() = module {
    singleOf(::UrlOpener)
}