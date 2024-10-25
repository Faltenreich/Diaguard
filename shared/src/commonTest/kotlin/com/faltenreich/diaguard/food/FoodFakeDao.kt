package com.faltenreich.diaguard.food

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.data.PagingPage

open class FoodFakeDao : FoodDao {

    private val cache = mutableStateListOf<Food.Local>()

    override fun transaction(transact: () -> Unit) {
        transact()
    }

    override fun create(
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
        sugar: Double?
    ) {
        cache += Food.Local(
            id = cache.size.toLong(),
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
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun getById(id: Long): Food.Local? {
        return cache.firstOrNull { it.id == id }
    }

    override fun getByUuid(uuid: String): Food.Local? {
        return cache.firstOrNull { it.uuid == uuid }
    }

    override fun getByUuids(uuids: List<String>): List<String> {
        return cache.filter { it.uuid in uuids }.map { it.uuid!! }
    }

    override fun getAll(
        showCommonFood: Boolean,
        showCustomFood: Boolean,
        showBrandedFood: Boolean,
        page: PagingPage
    ): List<Food.Local> {
        return cache.filter {
            showCommonFood && it.uuid == null && it.labels != null
                || showCustomFood && it.uuid == null && it.labels == null
                || showBrandedFood && it.uuid != null
        }
    }

    override fun getByQuery(
        query: String,
        showCommonFood: Boolean,
        showCustomFood: Boolean,
        showBrandedFood: Boolean,
        page: PagingPage
    ): List<Food.Local> {
        return cache
            .filter { it.name == query }
            .filter {
                showCommonFood && it.uuid == null && it.labels != null
                    || showCustomFood && it.uuid == null && it.labels == null
                    || showBrandedFood && it.uuid != null
            }
            .subList(page.page * page.pageSize, page.pageSize)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
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
        sugar: Double?
    ) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
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

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}