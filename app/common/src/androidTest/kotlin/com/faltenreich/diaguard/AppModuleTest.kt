package com.faltenreich.diaguard

import android.app.Activity
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import io.ktor.client.engine.HttpClientEngine
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
                Context::class,
                File::class,
                HttpClientEngine::class,
                SqlDriver::class,
            )
        )
    }
}