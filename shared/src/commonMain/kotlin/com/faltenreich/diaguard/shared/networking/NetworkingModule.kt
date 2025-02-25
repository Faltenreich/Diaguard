package com.faltenreich.diaguard.shared.networking

import com.faltenreich.diaguard.shared.networking.ktor.KtorClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun networkingModule() = module {
    singleOf(::UrlOpener)
    single<NetworkingClient> { KtorClient() }
}