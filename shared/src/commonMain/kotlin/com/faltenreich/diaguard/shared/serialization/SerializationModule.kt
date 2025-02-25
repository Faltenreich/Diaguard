package com.faltenreich.diaguard.shared.serialization

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun serializationModule() = module {
    singleOf(::Serialization)

}