package com.faltenreich.diaguard.data.preference

interface Preference<Store, Domain> {

    val key: String

    val default: Domain

    val onRead: (Store) -> Domain?

    val onWrite: (Domain) -> Store
}