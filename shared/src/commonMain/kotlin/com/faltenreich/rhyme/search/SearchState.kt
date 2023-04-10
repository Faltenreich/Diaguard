package com.faltenreich.rhyme.search

import com.faltenreich.rhyme.word.RhymeType
import com.faltenreich.rhyme.word.Word

sealed class SearchState(val query: String) {

    object Idle: SearchState("")

    class Loading(query: String): SearchState(query)

    class Error(query: String): SearchState(query)

    class Result(query: String, val words: Map<RhymeType, List<Word>>): SearchState(query)
}