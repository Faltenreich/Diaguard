package com.faltenreich.diaguard.backup.seed.query.food

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.localization.FakeLocalization
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FoodSeedQueriesTest {

    private fun getQueries(locale: Locale): FoodSeedQueries {
        return FoodSeedQueries(
            fileReader = SystemFileReader("src/commonTest/resources/seed/food.csv"),
            serialization = Serialization(),
            localization = FakeLocalization(locale),
        )
    }

    @Test
    fun `imports German seed`() {
        val queries = getQueries(locale = Locale("de"))
        val seed = queries.getAll()
        assertTrue(seed.isNotEmpty())
        assertEquals(seed[1].name, "Agavensirup")
    }

    @Test
    fun `imports French seed`() {
        val queries = getQueries(locale = Locale("fr"))
        val seed = queries.getAll()
        assertTrue(seed.isNotEmpty())
        assertEquals(seed[1].name, "Sirop d'agave")
    }

    @Test
    fun `imports Italian seed`() {
        val queries = getQueries(locale = Locale("it"))
        val seed = queries.getAll()
        assertTrue(seed.isNotEmpty())
        assertEquals(seed[1].name, "Sciroppo di agave")
    }

    @Test
    fun `imports English seed`() {
        val queries = getQueries(locale = Locale("en"))
        val seed = queries.getAll()
        assertTrue(seed.isNotEmpty())
        assertEquals(seed[1].name, "Agave syrup")
    }
}