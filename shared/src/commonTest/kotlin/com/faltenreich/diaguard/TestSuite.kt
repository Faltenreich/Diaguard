package com.faltenreich.diaguard

import androidx.annotation.CallSuper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.component.get
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

interface TestSuite : KoinTest {

    @BeforeTest
    @CallSuper
    fun beforeTest() {
        DependencyInjection.setup(modules = appModules() + testModules())
        Dispatchers.setMain(get())
    }

    @AfterTest
    @CallSuper
    fun afterTest() {
        Dispatchers.resetMain()
    }
}