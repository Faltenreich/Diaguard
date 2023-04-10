package com.faltenreich.diaguard.search.api

import com.faltenreich.diaguard.word.Word

interface WordMapper<T: Any> {

    fun map(input: T): Word
}