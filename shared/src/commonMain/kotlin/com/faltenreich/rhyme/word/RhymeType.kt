package com.faltenreich.rhyme.word

import com.faltenreich.rhyme.MR
import dev.icerock.moko.resources.StringResource

enum class RhymeType(val textResource: StringResource) {

    PURE(MR.strings.rhyme_type_pure),
    IMPURE(MR.strings.rhyme_type_impure),
    ;

    companion object {

        private const val PURE_THRESHOLD = 300

        fun fromScore(score: Int): RhymeType {
            return if (score >= PURE_THRESHOLD) PURE else IMPURE
        }
    }
}