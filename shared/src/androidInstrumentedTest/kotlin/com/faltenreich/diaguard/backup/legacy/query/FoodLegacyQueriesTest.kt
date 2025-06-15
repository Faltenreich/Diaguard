package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.test.FileFactory
import org.junit.Assert
import org.junit.Test

class FoodLegacyQueriesTest {

    private val database = SqliteDatabase(file = FileFactory.createFromAssets("diaguard.db"))
    private val dateTimeFactory = KotlinxDateTimeFactory()

    private val queries = FoodLegacyQueries(
        database = database,
        dateTimeFactory = dateTimeFactory,
    )

    @Test
    fun readsFood() {
        val expected = arrayOf(
            Food.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198076),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198076),
                uuid = null,
                name = "Onion quiche, baked (with cake dough)",
                brand = null,
                ingredients = "Onion quiche, baked (with cake dough)",
                labels = "Common food",
                carbohydrates = 15.0,
                energy = 175.0,
                fat = 10.0,
                fatSaturated = 5.0,
                fiber = 1.6,
                proteins = 5.0,
                salt = 4.1,
                sodium = 0.4,
                sugar = 5.1,
            ),
            Food.Legacy(
                id = 962,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198194),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198194),
                uuid = null,
                name = "Cola",
                brand = "",
                ingredients = "",
                labels = null,
                carbohydrates = 10.0,
                energy = -1.0,
                fat = -1.0,
                fatSaturated = -1.0,
                fiber = -1.0,
                proteins = -1.0,
                salt = -1.0,
                sodium = -1.0,
                sugar = -1.0,
            ),
        )
        val actual = queries.getAll().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }
}