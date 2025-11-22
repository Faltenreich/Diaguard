package com.faltenreich.diaguard.view

import com.faltenreich.diaguard.injection.androidActivity
import com.faltenreich.diaguard.view.window.AndroidWindowController
import com.faltenreich.diaguard.view.window.WindowController
import org.koin.dsl.module

internal actual fun viewPlatformModule() = module {
    factory<WindowController> { AndroidWindowController(androidActivity()) }
}