package com.faltenreich.diaguard.preference

import org.jetbrains.compose.resources.StringResource

interface Preference<Store, Domain> {

    val key: StringResource

    val default: Domain

    val onRead: (Store) -> Domain?

    val onWrite: (Domain) -> Store
}