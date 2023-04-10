package com.faltenreich.diaguard.search.api.rhymebrain

import com.faltenreich.diaguard.search.api.WordMapper
import com.faltenreich.diaguard.word.Word

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