package com.faltenreich.diaguard.serialization

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun serializationModule() = module {
    factoryOf(::Serialization)
}