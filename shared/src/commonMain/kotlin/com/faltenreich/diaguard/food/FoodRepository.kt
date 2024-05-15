package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.logging.Logger

class FoodRepository(
    private val dao: FoodDao,
    private val api: FoodApi,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
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
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
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

    suspend fun getByQuery(query: String, page: PagingPage): List<Food> {
        return if (query.isBlank()) {
            dao.getAll(page)
        } else {
            val foodFromApi = api.search(query, page)
            val uuids = foodFromApi.map(FoodFromApi::uuid)

            dao.transaction {
                val existing = dao.getByUuids(uuids)

                // We do not update to avoid altering data edited by user
                val update = foodFromApi.filterNot { it.uuid in existing }
                update.forEach { food ->
                    create(
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
                    Logger.debug("Stored $food")
                }
            }
            return dao.getByQuery(query, page)
        }
    }

    fun update(
        id: Long,
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
            updatedAt = dateTimeFactory.now(),
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