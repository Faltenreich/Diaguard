package com.faltenreich.rhyme.search.api.rhymebrain

import com.faltenreich.rhyme.search.api.WordMapper
import com.faltenreich.rhyme.word.Word

class RhymeBrainWordMapper: WordMapper<RhymeBrainWord> {

    override fun map(input: RhymeBrainWord): Word {
        return Word(
            name = input.word,
            syllables = input.syllables.toIntOrNull() ?: 1,
            frequency = input.frequency,
            score = input.score,
            isOffensive = input.flags.contains("a"),
        )
    }
}