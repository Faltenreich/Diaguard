package com.faltenreich.rhyme.search

import com.faltenreich.rhyme.language.Language
import com.faltenreich.rhyme.search.api.SearchApi
import com.faltenreich.rhyme.word.Word
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