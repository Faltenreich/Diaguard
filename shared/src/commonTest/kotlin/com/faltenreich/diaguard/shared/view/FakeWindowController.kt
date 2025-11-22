package com.faltenreich.diaguard.shared.view

import com.faltenreich.diaguard.view.window.WindowController

class FakeWindowController : WindowController {

    override suspend fun setIsAppearanceLightStatusBars(isAppearanceLightStatusBars: Boolean) = Unit
}