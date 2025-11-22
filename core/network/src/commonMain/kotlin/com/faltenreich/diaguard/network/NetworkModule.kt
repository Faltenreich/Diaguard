package com.faltenreich.diaguard.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

private const val TIMEOUT_IN_SECONDS = 10

fun networkModule() = module {
    single {
        HttpClient {
            install(ContentNegotiation) { json() }
            install(HttpTimeout) {
                requestTimeoutMillis = TIMEOUT_IN_SECONDS.seconds.inWholeMilliseconds
            }
        }
    }
    singleOf(::KtorClient) bind NetworkClient::class
}