package com.faltenreich.diaguard.word

import dev.icerock.moko.resources.StringResource

enum class RhymeType(val textResource: StringResource) {

    PURE(com.faltenreich.diaguard.MR.strings.rhyme_type_pure),
    IMPURE(com.faltenreich.diaguard.MR.strings.rhyme_type_impure),
    ;

    companion object {

        private const val PURE_THRESHOLD = 300

        fun fromScore(score: Int): RhymeType {
            return if (score >= PURE_THRESHOLD) PURE else IMPURE
        }
    }
}