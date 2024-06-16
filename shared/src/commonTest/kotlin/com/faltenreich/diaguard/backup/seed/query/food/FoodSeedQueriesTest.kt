package com.faltenreich.diaguard.backup.seed.query.food

import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlin.test.Test
import kotlin.test.assertTrue

class FoodSeedQueriesTest {

    private val seed = FoodSeedQueries(
        fileReader = SystemFileReader("src/commonTest/resources/seed/food.csv"),
        serialization = Serialization(),
    )

    @Test
    fun testSeed() {
        assertTrue(seed.getAll().isNotEmpty())
    }
}