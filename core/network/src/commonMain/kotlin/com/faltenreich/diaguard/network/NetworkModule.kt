package com.faltenreich.diaguard.network

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun networkModule() = module {
    singleOf(::KtorClient) bind NetworkClient::class
}