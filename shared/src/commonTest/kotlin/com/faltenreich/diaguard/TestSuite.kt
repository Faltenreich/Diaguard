package com.faltenreich.diaguard

import androidx.annotation.CallSuper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

interface TestSuite : KoinTest {

    @BeforeTest
    @CallSuper
    fun beforeTest() {
        startKoin { modules(appModules() + testModules()) }
        Dispatchers.setMain(dispatcher = get())
    }

    @AfterTest
    @CallSuper
    fun afterTest() {
        stopKoin()
        Dispatchers.resetMain()
    }
}