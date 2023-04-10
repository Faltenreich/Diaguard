package com.faltenreich.diaguard.shared.clipboard

import org.koin.dsl.bind
import org.koin.dsl.module

fun clipboardModule() = module {
    single { PlatformClipboard() } bind Clipboard::class
}