package com.faltenreich.diaguard.shared.view

import org.koin.dsl.module

actual fun windowModule() = module {
    factory<WindowController> { AndroidWindowController() }
}