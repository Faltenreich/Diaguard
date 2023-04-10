package com.faltenreich.rhyme.word

import kotlin.test.Test
import kotlin.test.assertEquals

class RhymeTypeTest {

    @Test
    fun `is pure with score higher or equal than 300`() {
        assertEquals(RhymeType.PURE, RhymeType.fromScore(300))
        assertEquals(RhymeType.PURE, RhymeType.fromScore(301))
    }

    @Test
    fun `is impure with score less than 300`() {
        assertEquals(RhymeType.IMPURE, RhymeType.fromScore(299))
    }
}