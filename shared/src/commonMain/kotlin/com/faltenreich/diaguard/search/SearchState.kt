package com.faltenreich.diaguard.search

import com.faltenreich.diaguard.word.RhymeType
import com.faltenreich.diaguard.word.Word

sealed class SearchState(val query: String) {

    object Idle: SearchState("")

    class Loading(query: String): SearchState(query)

    class Error(query: String): SearchState(query)

    class Result(query: String, val words: Map<RhymeType, List<Word>>): SearchState(query)
}