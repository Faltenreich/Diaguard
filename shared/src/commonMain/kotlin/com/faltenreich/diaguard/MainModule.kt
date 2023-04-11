package com.faltenreich.diaguard

import com.faltenreich.diaguard.shared.architecture.coroutineModule
import com.faltenreich.diaguard.shared.clipboard.clipboardModule
import com.faltenreich.diaguard.shared.localization.localizationModule

fun mainModule() = listOf(
    coroutineModule(),
    clipboardModule(),
    localizationModule(),
)