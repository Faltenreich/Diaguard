package com.faltenreich.diaguard.backup.legacy.dao

import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyActivitySqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyBloodPressureSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyBloodSugarSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyHbA1cSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyInsulinSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyMealSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyOxygenSaturationSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyPulseSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyWeightSqliteDao
import com.faltenreich.diaguard.measurement.value.MeasurementValue

class LegacyMeasurementValueSqliteDao(
    private val bloodSugarDao: LegacyBloodSugarSqliteDao,
    private val insulinDao: LegacyInsulinSqliteDao,
    private val mealDao: LegacyMealSqliteDao,
    private val activityDao: LegacyActivitySqliteDao,
    private val hba1cDao: LegacyHbA1cSqliteDao,
    private val weightDao: LegacyWeightSqliteDao,
    private val pulseDao: LegacyPulseSqliteDao,
    private val bloodPressureDao: LegacyBloodPressureSqliteDao,
    private val oxygenSaturationDao: LegacyOxygenSaturationSqliteDao,
) {

    fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return bloodSugarDao.getMeasurementValues() +
            insulinDao.getMeasurementValues() +
            mealDao.getMeasurementValues() +
            activityDao.getMeasurementValues() +
            hba1cDao.getMeasurementValues() +
            weightDao.getMeasurementValues() +
            pulseDao.getMeasurementValues() +
            bloodPressureDao.getMeasurementValues() +
            oxygenSaturationDao.getMeasurementValues()
    }
}