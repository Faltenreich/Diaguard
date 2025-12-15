package com.faltenreich.diaguard.data.preview

import com.faltenreich.diaguard.data.fake.FakeFactory
import com.faltenreich.diaguard.datetime.DayOfWeek

class PreviewScope : FakeFactory {

    @Suppress("MagicNumber")
    fun DayOfWeek.localized() = toString()
        .take(3)
        .lowercase()
        .replaceFirstChar(Char::uppercase)
}