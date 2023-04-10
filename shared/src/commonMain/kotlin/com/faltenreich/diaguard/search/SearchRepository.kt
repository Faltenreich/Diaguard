package com.faltenreich.diaguard.search

import com.faltenreich.diaguard.language.Language
import com.faltenreich.diaguard.search.api.SearchApi
import com.faltenreich.diaguard.word.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepository(
    private val searchApi: SearchApi,
) {

    fun search(query: String, language: Language): Flow<List<Word>> = flow {
        val words = searchApi.search(query, language).sortedByDescending(Word::score)
        emit(words)
    }
}