package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.food.api.openfoodfacts.OpenFoodFactsApi
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach

class FoodRepository(
    private val dao: FoodDao,
    private val api: OpenFoodFactsApi,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        uuid: String?,
        name: String,
        brand: String?,
        ingredients: String?,
        labels: String?,
        carbohydrates: Double,
        energy: Double?,
        fat: Double?,
        fatSaturated: Double?,
        fiber: Double?,
        proteins: Double?,
        salt: Double?,
        sodium: Double?,
        sugar: Double?,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            uuid = uuid,
            name = name,
            brand = brand,
            ingredients = ingredients,
            labels = labels,
            carbohydrates = carbohydrates,
            energy = energy,
            fat = fat,
            fatSaturated = fatSaturated,
            fiber = fiber,
            proteins = proteins,
            salt = salt,
            sodium = sodium,
            sugar = sugar,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(foodList: List<FoodFromApi>) {
        // TODO: Bulk insert
        val now = dateTimeFactory.now()
        foodList.forEach { food ->
            val existing = getByUuid(food.uuid)
            if (existing != null) {
                Logger.debug("Updating food: $food")
                update(
                    id = existing.id,
                    updatedAt = now,
                    name = food.name,
                    brand = food.brand,
                    ingredients = food.ingredients,
                    labels = food.labels,
                    carbohydrates = food.carbohydrates,
                    energy = food.energy,
                    fat = food.fat,
                    fatSaturated = food.fatSaturated,
                    fiber = food.fiber,
                    proteins = food.proteins,
                    salt = food.salt,
                    sodium = food.sodium,
                    sugar = food.sugar,
                )
            } else {
                Logger.debug("Creating food: $food")
                create(
                    createdAt = now,
                    updatedAt = now,
                    uuid = food.uuid,
                    name = food.name,
                    brand = food.brand,
                    ingredients = food.ingredients,
                    labels = food.labels,
                    carbohydrates = food.carbohydrates,
                    energy = food.energy,
                    fat = food.fat,
                    fatSaturated = food.fatSaturated,
                    fiber = food.fiber,
                    proteins = food.proteins,
                    salt = food.salt,
                    sodium = food.sodium,
                    sugar = food.sugar,
                )
            }
        }
    }

    fun observeAll(): Flow<List<Food>> {
        return dao.observeAll()
    }

    fun getByUuid(uuid: String): Food? {
        return dao.getByUuid(uuid)
    }

    fun observeByQuery(query: String): Flow<List<Food>> {
        return observeByQueryRemotely(query).flatMapLatest { observeByQueryLocally(query) }
    }

    private fun observeByQueryRemotely(query: String): Flow<List<FoodFromApi>> {
        return api.search(query, page = 0).onEach(::create)
    }

    private fun observeByQueryLocally(query: String): Flow<List<Food>> {
        return dao.observeByQuery(query)
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        carbohydrates: Double,
        brand: String? = null,
        ingredients: String? = null,
        labels: String? = null,
        energy: Double? = null,
        fat: Double? = null,
        fatSaturated: Double? = null,
        fiber: Double? = null,
        proteins: Double? = null,
        salt: Double? = null,
        sodium: Double? = null,
        sugar: Double? = null,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            brand = brand,
            ingredients = ingredients,
            labels = labels,
            carbohydrates = carbohydrates,
            energy = energy,
            fat = fat,
            fatSaturated = fatSaturated,
            fiber = fiber,
            proteins = proteins,
            salt = salt,
            sodium = sodium,
            sugar = sugar,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}