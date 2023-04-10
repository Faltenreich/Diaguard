@file:OptIn(ExperimentalCoroutinesApi::class)

package com.faltenreich.rhyme.search

import com.faltenreich.rhyme.language.LanguageRepository
import com.faltenreich.rhyme.word.RhymeType
import com.faltenreich.rhyme.word.Word
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class SearchUseCase(
    private val searchRepository: SearchRepository,
    private val languageRepository: LanguageRepository,
) {

    operator fun invoke(query: String): Flow<Map<RhymeType, List<Word>>> {
        return languageRepository.currentLanguage
            .flatMapLatest { language -> searchRepository.search(query, language) }
            .map { words -> words.groupBy { word -> RhymeType.fromScore(word.score) } }
    }
}