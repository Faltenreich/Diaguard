package com.faltenreich.diaguard

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.module.Module
import org.koin.test.verify.verify

@RunWith(AndroidJUnit4::class)
class DependencyInjectionTest {

    @Test
    fun verifyDependencyGraph() {
        // FIXME: MissingKoinDefinitionException: Missing definition type 'android.content.Context' in definition
        appModules().forEach(Module::verify)
    }
}