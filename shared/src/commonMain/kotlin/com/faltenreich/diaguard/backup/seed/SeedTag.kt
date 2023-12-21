package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedTag(
    val en: String,
    val de: String,
    val fr: String,
    val es: String,
    val it: String,
)