package com.faltenreich.diaguard.search.api

import com.faltenreich.diaguard.language.Language

interface SearchHost {

    fun getUrl(query: String?, language: Language): String
}