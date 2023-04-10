package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun coroutineModule() = module {
    single<CoroutineDispatcher> { StandardTestDispatcher() }
    single<CoroutineContext> { StandardTestDispatcher() }
}