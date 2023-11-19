package com.faltenreich.diaguard.backup.seed.data

import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlin.test.Test
import kotlin.test.assertTrue

class FoodSeedTest {

    private val seed = FoodSeed(
        fileReader = SystemFileReader("src/commonTest/resources/food.csv"),
        serialization = Serialization(),
    )

    @Test
    fun testSeed() {
        assertTrue(seed().isNotEmpty())
    }
}