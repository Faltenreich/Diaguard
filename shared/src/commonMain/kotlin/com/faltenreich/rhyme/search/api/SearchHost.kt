package com.faltenreich.rhyme.search.api

import com.faltenreich.rhyme.language.Language

interface SearchHost {

    fun getUrl(query: String?, language: Language): String
}