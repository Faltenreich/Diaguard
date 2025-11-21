package com.faltenreich.diaguard.network

import org.koin.dsl.module

fun networkModule() = module {
    single<NetworkClient> { KtorClient() }
}