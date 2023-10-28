package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.shared.database.sqldelight.FoodQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementPropertySqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class FoodSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementPropertySqlDelightMapper = inject(),
) : FoodDao, SqlDelightDao<FoodQueries> {

    override fun getQueries(api: SqlDelightApi): FoodQueries {
        return api.foodQueries
    }

    override fun create(
        createdAt: DateTime,
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
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeAll(): Flow<List<Food>> {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }


    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}