package com.faltenreich.diaguard.shared.view

class FakeWindowController : WindowController {

    override suspend fun setIsAppearanceLightStatusBars(isAppearanceLightStatusBars: Boolean) = Unit
}