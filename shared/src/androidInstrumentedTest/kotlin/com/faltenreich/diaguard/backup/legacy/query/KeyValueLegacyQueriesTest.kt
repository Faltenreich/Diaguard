package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.food.ShowBrandedFoodPreference
import com.faltenreich.diaguard.preference.food.ShowCommonFoodPreference
import com.faltenreich.diaguard.preference.food.ShowCustomFoodPreference
import com.faltenreich.diaguard.preference.screen.StartScreen
import com.faltenreich.diaguard.preference.screen.StartScreenPreference
import com.faltenreich.diaguard.preference.version.VersionCodePreference
import com.faltenreich.diaguard.shared.keyvalue.FakeKeyValueStore
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class KeyValueLegacyQueriesTest {

    @Test
    fun readTheme() = runTest {
        listOf(
            "0" to ColorScheme.LIGHT,
            "1" to ColorScheme.DARK,
            "2" to ColorScheme.SYSTEM,
            "3" to ColorScheme.SYSTEM,
        ).forEach { (legacy, result) ->
            val keyValueStore = FakeKeyValueStore("theme" to legacy)
            val queries = KeyValueLegacyQueries(keyValueStore)

            Assert.assertEquals(
                result,
                queries.getPreference(ColorSchemePreference),
            )
        }
    }

    @Test
    fun readDecimalPlaces() = runTest {
        val decimalPlaces = 2

        val keyValueStore = FakeKeyValueStore("Decimal places" to decimalPlaces)
        val queries = KeyValueLegacyQueries(keyValueStore)

        Assert.assertEquals(
            decimalPlaces,
            queries.getPreference(DecimalPlacesPreference),
        )
    }

    @Test
    fun readFoodPreferences() = runTest {
        val keyValueStore = FakeKeyValueStore(
            "showBrandedFood" to true,
            "showCommonFood" to false,
            "showCustomFood" to true,
        )
        val queries = KeyValueLegacyQueries(keyValueStore)

        Assert.assertEquals(
            true,
            queries.getPreference(ShowBrandedFoodPreference),
        )
        Assert.assertEquals(
            false,
            queries.getPreference(ShowCommonFoodPreference),
        )
        Assert.assertEquals(
            true,
            queries.getPreference(ShowCustomFoodPreference),
        )
    }

    @Test
    fun readStartScreen() = runTest {
        listOf(
            "0" to StartScreen.DASHBOARD,
            "1" to StartScreen.TIMELINE,
            "2" to StartScreen.LOG,
        ).forEach { (legacy, result) ->
            val keyValueStore = FakeKeyValueStore("startscreen" to legacy)
            val queries = KeyValueLegacyQueries(keyValueStore)

            Assert.assertEquals(
                result,
                queries.getPreference(StartScreenPreference),
            )
        }
    }

    @Test
    fun readVersionCode() = runTest {
        val versionCode = 1337

        val keyValueStore = FakeKeyValueStore("versionCode" to versionCode)
        val queries = KeyValueLegacyQueries(keyValueStore)

        Assert.assertEquals(
            versionCode,
            queries.getPreference(VersionCodePreference),
        )
    }
}