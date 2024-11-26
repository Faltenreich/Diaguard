package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.shared.keyvalue.FakeKeyValueStore
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class KeyValueLegacyQueriesTest {

    @Test
    fun readTheme() = runTest {
        val keyValueStore = FakeKeyValueStore("theme" to "1")
        val queries = KeyValueLegacyQueries(keyValueStore)
        Assert.assertEquals(
            ColorScheme.DARK,
            queries.getPreference(ColorSchemePreference),
        )
    }
}