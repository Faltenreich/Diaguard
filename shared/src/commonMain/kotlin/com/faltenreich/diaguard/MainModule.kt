package com.faltenreich.diaguard

import com.faltenreich.diaguard.language.languageModule
import com.faltenreich.diaguard.search.searchModule
import com.faltenreich.diaguard.shared.architecture.coroutineModule
import com.faltenreich.diaguard.shared.clipboard.clipboardModule
import com.faltenreich.diaguard.shared.localization.localizationModule
import com.faltenreich.diaguard.word.wordModule

fun mainModule() = listOf(
    coroutineModule(),
    clipboardModule(),
    localizationModule(),
    languageModule(),
    searchModule(),
    wordModule(),
)