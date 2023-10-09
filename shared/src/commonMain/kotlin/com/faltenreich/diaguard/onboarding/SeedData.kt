package com.faltenreich.diaguard.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class Localization(
    val en: String,
)

@Serializable
data class Property(
    val name: List<Localization>,
    val types: List<Type>,
) {

    @Serializable
    data class Type(
        val name: List<Localization>,
        val units: List<Unit>,
    ) {

        @Serializable
        data class Unit(
            val factor: Double,
            val name: List<Localization>,
            val abbreviation: List<Localization>,
        )
    }
}