package com.faltenreich.diaguard.word

data class Word(
    val name: String,
    val syllables: Int,
    val frequency: Int,
    val score: Int,
    val isOffensive: Boolean,
)