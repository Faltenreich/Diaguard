package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun coroutineModule() = module {
    single<CoroutineDispatcher> { StandardTestDispatcher() }
    single<CoroutineContext> { StandardTestDispatcher() }
    single<CoroutineScope> { TestScope(context = get()) }
}