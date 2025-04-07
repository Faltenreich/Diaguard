package com.faltenreich.diaguard.backup.seed.query.measurement

import com.faltenreich.diaguard.backup.seed.query.SeedQueries
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

class MeasurementUnitSeedQueries : SeedQueries<MeasurementUnit.Seed> {

    override fun getAll(): List<MeasurementUnit.Seed> {
        return emptyList()
    }
}