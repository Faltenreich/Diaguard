package com.faltenreich.rhyme

import com.faltenreich.rhyme.language.languageModule
import com.faltenreich.rhyme.search.searchModule
import com.faltenreich.rhyme.shared.architecture.coroutineModule
import com.faltenreich.rhyme.shared.clipboard.clipboardModule
import com.faltenreich.rhyme.shared.localization.localizationModule
import com.faltenreich.rhyme.word.wordModule

fun mainModule() = listOf(
    coroutineModule(),
    clipboardModule(),
    localizationModule(),
    languageModule(),
    searchModule(),
    wordModule(),
)