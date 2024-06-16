package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.database.sqlite.getDouble
import com.faltenreich.diaguard.shared.database.sqlite.getLong
import com.faltenreich.diaguard.shared.database.sqlite.getString

class FoodLegacyQueries(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) : LegacyQueries<Food.Legacy> {

    override fun getAll(): List<Food.Legacy> {
        val food = mutableListOf<Food.Legacy>()
        database.query("food") {
            val id = getLong("_id") ?: return@query
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val name = getString("name") ?: return@query
            val carbohydrates = getDouble("carbohydrates") ?: return@query
            food.add(
                Food.Legacy(
                    id = id,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    uuid = getString("serverId"),
                    name = name,
                    brand = getString("brand"),
                    ingredients = getString("ingredients"),
                    labels = getString("labels"),
                    carbohydrates = carbohydrates,
                    energy = getDouble("energy"),
                    fat = getDouble("fat"),
                    fatSaturated = getDouble("fatSaturated"),
                    fiber = getDouble("fiber"),
                    proteins = getDouble("proteins"),
                    salt = getDouble("salt"),
                    sodium = getDouble("sodium"),
                    sugar = getDouble("sugar"),
                )
            )
        }
        return food
    }
}