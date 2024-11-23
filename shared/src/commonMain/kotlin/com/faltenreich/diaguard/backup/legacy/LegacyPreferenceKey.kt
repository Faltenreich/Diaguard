package com.faltenreich.diaguard.backup.legacy

import kotlin.reflect.KClass

enum class LegacyPreferenceKey(val key: String, val kClass: KClass<*>) {
    VERSION_CODE("versionCode", Int::class),
    THEME("theme", String::class),
}