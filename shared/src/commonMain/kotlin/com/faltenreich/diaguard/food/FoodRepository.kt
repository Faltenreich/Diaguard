package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.api.openfoodfacts.OpenFoodFactsApi
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

    // TODO: Paginate
    fun observeByQuery(query: String): Flow<List<Food>> {
        return if (query.isBlank()) {
            dao.observeAll()
        } else {
            api.search(query, page = 0)
                .onEach { dao.createOrUpdate(foodFromApi = it, at = dateTimeFactory.now()) }
                .flatMapLatest { dao.observeByQuery(query) }
        }
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