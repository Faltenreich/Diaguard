package com.faltenreich.diaguard.backup.legacy

sealed interface LegacyPreference {

    data class Boolean(
        val key: LegacyPreferenceKey,
        val value: kotlin.Boolean,
    ) : LegacyPreference

    data class Int(
        val key: LegacyPreferenceKey,
        val value: kotlin.Int,
    ) : LegacyPreference

    data class Long(
        val key: LegacyPreferenceKey,
        val value: kotlin.Long,
    ) : LegacyPreference

    data class Float(
        val key: LegacyPreferenceKey,
        val value: kotlin.Float,
    ) : LegacyPreference

    data class String(
        val key: LegacyPreferenceKey,
        val value: kotlin.String,
    ) : LegacyPreference
}