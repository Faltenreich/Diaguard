package com.faltenreich.diaguard.search

import com.faltenreich.diaguard.language.Language
import com.faltenreich.diaguard.search.api.SearchApi
import com.faltenreich.diaguard.word.Word

class FakeSearchApi : SearchApi {

    override suspend fun search(query: String, language: Language): List<Word> {
        return emptyList()
    }
}