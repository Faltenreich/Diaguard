package com.faltenreich.diaguard.startup.seed.query.tag

import kotlinx.serialization.Serializable

@Serializable
data class TagFromFile(
    val en: String,
    val de: String,
    val fr: String,
    val es: String,
    val it: String,
)