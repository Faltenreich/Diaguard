package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.test.FileFactory
import org.junit.Assert
import org.junit.Test

class FoodEatenLegacyQueriesTest {

    private val database = SqliteDatabase(file = FileFactory.createFromAssets("diaguard.db"))
    private val dateTimeFactory = KotlinxDateTimeFactory()

    private val queries = FoodEatenLegacyQueries(
        database = database,
        dateTimeFactory = dateTimeFactory,
    )

    @Test
    fun readsFoodEaten() {
        val expected = arrayOf(
            FoodEaten.Legacy(
                createdAt = dateTimeFactory.dateTime(millis = 1717865198225),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198225),
                amountInGrams = 100.0,
                foodId = 962,
                mealId = 1,
            ),
        )
        val actual = queries.getAll().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }
}