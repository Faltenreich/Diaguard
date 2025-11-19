package com.faltenreich.diaguard.shared.view

import com.faltenreich.diaguard.injection.androidActivity
import org.koin.dsl.module

actual fun windowModule() = module {
    factory<WindowController> { AndroidWindowController(androidActivity()) }
}