package com.faltenreich.rhyme.shared.architecture

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun coroutineModule() = module {
    single<CoroutineContext> { Dispatchers.Default }
    single<CoroutineDispatcher> { Dispatchers.Default }
}