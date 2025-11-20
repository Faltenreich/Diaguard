package com.faltenreich.diaguard.networking

import com.faltenreich.diaguard.networking.ktor.KtorClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun networkingModule() = module {
    factoryOf(::UrlOpener)
    single<NetworkingClient> { KtorClient() }
}