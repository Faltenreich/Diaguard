package com.faltenreich.diaguard

import android.app.Activity
import android.content.Context
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import java.io.File

class AppModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun dependencyGraphIsValid() {
        appModule().verify(
            extraTypes = listOf(
                Activity::class,
                File::class,
                Context::class,
            )
        )
    }
}