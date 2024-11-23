package com.faltenreich.diaguard.backup.legacy

sealed interface LegacyPreference {

    data class Boolean(
        val key: kotlin.String,
        val value: kotlin.Boolean,
    ) : LegacyPreference

    data class Int(
        val key: kotlin.String,
        val value: kotlin.Int,
    ) : LegacyPreference

    data class Long(
        val key: kotlin.String,
        val value: kotlin.Long,
    ) : LegacyPreference

    data class Float(
        val key: kotlin.String,
        val value: kotlin.Float,
    ) : LegacyPreference

    data class String(
        val key: kotlin.String,
        val value: kotlin.String,
    ) : LegacyPreference
}