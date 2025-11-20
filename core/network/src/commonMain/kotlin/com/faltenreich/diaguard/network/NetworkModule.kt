package com.faltenreich.diaguard.network

import com.faltenreich.diaguard.network.KtorClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun networkModule() = module {
    factoryOf(::UrlOpener)
    single<NetworkClient> { KtorClient() }
}