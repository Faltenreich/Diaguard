package com.faltenreich.diaguard.view.window

import com.faltenreich.diaguard.injection.androidActivity
import org.koin.dsl.module

actual fun windowModule() = module {
    factory<WindowController> { AndroidWindowController(androidActivity()) }
}