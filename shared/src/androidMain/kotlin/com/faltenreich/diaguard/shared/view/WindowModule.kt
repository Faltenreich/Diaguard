package com.faltenreich.diaguard.shared.view

import com.faltenreich.diaguard.localization.di.androidActivity
import org.koin.dsl.module

actual fun windowModule() = module {
    factory<WindowController> { AndroidWindowController(androidActivity()) }
}