package com.faltenreich.diaguard.search.api

import com.faltenreich.diaguard.language.Language
import com.faltenreich.diaguard.word.Word

interface SearchApi {

    suspend fun search(query: String, language: Language): List<Word>
}