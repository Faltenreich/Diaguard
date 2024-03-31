package com.faltenreich.diaguard.preference

import dev.icerock.moko.resources.StringResource

sealed class Preference<Store, Domain>(
    val key: StringResource,
    val default: Domain,
    val onRead: (Store) -> Domain?,
    val onWrite: (Domain) -> Store,
)