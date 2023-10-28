package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedFood(
    val de: String,
    val fr: String,
    val it: String,
    val en: String,
    val carbohydrates: String,
    val energy: String,
    val fat: String,
    val fatSaturated: String,
    val fiber: String,
    val proteins: String,
    val salt: String,
    val sodium: String,
    val sugar: String,
)