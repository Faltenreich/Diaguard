package com.faltenreich.diaguard.preference.list.item

import dev.icerock.moko.resources.StringResource

interface ListPreference {
    val stableId: Int
    val labelResource: StringResource
}