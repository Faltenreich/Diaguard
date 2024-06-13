package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.shared.data.PagingPage

class FoodRepository(
    private val dao: FoodDao,
    private val api: FoodApi,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(food: Food.Legacy): Long = with(food) {
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

    fun create(food: Food.User): Long = with(food) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            uuid = null,
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

    fun create(food: Food.Remote): Long = with(food) {
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

    suspend fun getByQuery(query: String, page: PagingPage): List<Food.Local> {
        return if (query.isBlank()) {
            dao.getAll(page)
        } else {
            val foodFromApi = api.search(query, page)
            val uuids = foodFromApi.map(FoodFromApi::uuid)

            dao.transaction {
                val existing = dao.getByUuids(uuids)

                // We do not update to avoid altering data edited by user
                val new = foodFromApi.filterNot { it.uuid in existing }
                new.forEach { food ->
                    val remote = Food.Remote(
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
                    create(remote)
                }
            }
            // FIXME: Compensate delta of response from FoodApi
            return dao.getByQuery(query, page)
        }
    }

    fun update(food: Food.Local) = with(food) {
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

    fun delete(food: Food.Local) {
        dao.deleteById(food.id)
    }
}