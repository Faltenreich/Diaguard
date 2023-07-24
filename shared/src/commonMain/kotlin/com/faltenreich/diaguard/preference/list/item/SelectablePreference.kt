package com.faltenreich.diaguard.preference.list.item

import dev.icerock.moko.resources.StringResource

interface SelectablePreference {
    val stableId: Int
    val labelResource: StringResource
}