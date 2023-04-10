@file:OptIn(ExperimentalCoroutinesApi::class)

package com.faltenreich.rhyme.shared.architecture

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun coroutineModule() = module {
    single<CoroutineDispatcher> { StandardTestDispatcher() }
    single<CoroutineContext> { StandardTestDispatcher() }
}