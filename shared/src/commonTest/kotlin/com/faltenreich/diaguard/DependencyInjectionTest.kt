package com.faltenreich.diaguard

import org.koin.test.check.checkModules
import kotlin.test.Test

class DependencyInjectionTest {

    @Test
    fun verifyDependencyGraph() {
        DependencyInjection.setup { checkModules() }
    }
}