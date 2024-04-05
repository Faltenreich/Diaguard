package com.faltenreich.diaguard.preference

import org.jetbrains.compose.resources.StringResource

sealed class Preference<Store, Domain>(
    val key: StringResource,
    val default: Domain,
    val onRead: (Store) -> Domain?,
    val onWrite: (Domain) -> Store,
)