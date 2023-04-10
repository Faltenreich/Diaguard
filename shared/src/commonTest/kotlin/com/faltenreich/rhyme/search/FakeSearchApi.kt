package com.faltenreich.rhyme.search

import com.faltenreich.rhyme.language.Language
import com.faltenreich.rhyme.search.api.SearchApi
import com.faltenreich.rhyme.word.Word

class FakeSearchApi : SearchApi {

    override suspend fun search(query: String, language: Language): List<Word> {
        return emptyList()
    }
}