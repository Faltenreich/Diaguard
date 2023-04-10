package com.faltenreich.rhyme.search.api.rhymebrain

import com.faltenreich.rhyme.language.Language
import com.faltenreich.rhyme.shared.networking.KtorClient
import com.faltenreich.rhyme.shared.networking.NetworkingClient
import com.faltenreich.rhyme.search.api.SearchApi
import com.faltenreich.rhyme.search.api.WordMapper
import com.faltenreich.rhyme.shared.serialization.JsonSerialization
import com.faltenreich.rhyme.word.Word
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