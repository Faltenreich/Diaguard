package com.faltenreich.diaguard.shared.keyvalue

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class SharedPreferencesTest {

    @Test
    @Ignore("Returns always null")
    fun readsSharedPreferences() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().context
        val sharedPreferences = SharedPreferences(context)

        val (key, value) = "theme" to "0"

        sharedPreferences.write<String>(key, value)

        sharedPreferences.read<String>(key).test {
            Assert.assertEquals(
                value,
                awaitItem(),
            )
        }
    }
}