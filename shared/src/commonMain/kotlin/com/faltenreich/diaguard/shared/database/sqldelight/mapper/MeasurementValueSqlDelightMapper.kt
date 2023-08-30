package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject

class MeasurementValueSqlDelightMapper(
    private val typeMapper: MeasurementTypeSqlDelightMapper = inject(),
    private val propertyMapper: MeasurementPropertySqlDelightMapper = inject(),
    private val entryMapper: EntrySqlDelightMapper = inject(),
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        value: Double,
        typeId: Long,
        entryId: Long,
    ): MeasurementValue {
        return MeasurementValue(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
            value = value,
            typeId = typeId,
            entryId = entryId,
        )
    }

    fun map(
        valueId: Long,
        valueCreatedAt: String,
        valueUpdatedAt: String,
        valueValue: Double,
        valueTypeId: Long,
        valueEntryId: Long,
        typeId: Long,
        typeCreatedAt: String,
        typeUpdatedAt: String,
        typeName: String,
        typeSortIndex: Long,
        typePropertyId: Long,
        propertyId: Long,
        propertyCreatedAt: String,
        propertyUpdatedAt: String,
        propertyName: String,
        propertyIcon: String?,
        propertySortIndex: Long,
        propertyIsCustom: Long,
        entryId: Long,
        entryCreatedAt: String,
        entryUpdatedAt: String,
        entryDateTime: String,
        entryNote: String?,
    ): MeasurementValue {
        return MeasurementValue(
            id = valueId,
            createdAt = DateTime(isoString = valueCreatedAt),
            updatedAt = DateTime(isoString = valueUpdatedAt),
            value = valueValue,
            typeId = valueTypeId,
            entryId = valueEntryId,
        ).apply {
            type = typeMapper.map(
                id = typeId,
                createdAt = typeCreatedAt,
                updatedAt = typeUpdatedAt,
                name = typeName,
                sortIndex = typeSortIndex,
                propertyId = typePropertyId,
            ).apply {
                property = propertyMapper.map(
                    id = propertyId,
                    createdAt = propertyCreatedAt,
                    updatedAt = propertyUpdatedAt,
                    name = propertyName,
                    icon = propertyIcon,
                    sortIndex = propertySortIndex,
                    isCustom = propertyIsCustom,
                )
            }
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            )
        }
    }
}