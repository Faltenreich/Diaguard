package com.faltenreich.diaguard.test

import androidx.annotation.CallSuper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class TestSuite(private val module: Module) : KoinTest {

    @BeforeTest
    @CallSuper
    open fun beforeTest() {
        startKoin { modules(module) }
        Dispatchers.setMain(dispatcher = StandardTestDispatcher())
    }

    @AfterTest
    @CallSuper
    open fun afterTest() {
        stopKoin()
        Dispatchers.resetMain()
    }
}