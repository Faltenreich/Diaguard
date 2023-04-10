package com.faltenreich.diaguard.search.api.rhymebrain

import com.faltenreich.diaguard.language.Language
import com.faltenreich.diaguard.search.api.SearchApi
import com.faltenreich.diaguard.search.api.WordMapper
import com.faltenreich.diaguard.shared.networking.KtorClient
import com.faltenreich.diaguard.shared.networking.NetworkingClient
import com.faltenreich.diaguard.shared.serialization.JsonSerialization
import com.faltenreich.diaguard.word.Word
import io.ktor.http.*

class RhymeBrainApi(
    private val networkingClient: NetworkingClient = KtorClient(),
    private val serialization: JsonSerialization = JsonSerialization(),
    private val mapper: WordMapper<RhymeBrainWord> = RhymeBrainWordMapper(),
): SearchApi {

    override suspend fun search(query: String, language: Language): List<Word> {
        val lang = language.code.encodeURLParameter()
        val word = query.encodeURLParameter()
        val url = "$HOST/talk?function=getRhymes&lang=$lang&word=$word"
        val json = networkingClient.request(url)
        val dtoList = serialization.decode<List<RhymeBrainWord>>(json)
        return dtoList.map(mapper::map)
    }

    companion object {

        private const val HOST = "https://rhymebrain.com"
    }
}