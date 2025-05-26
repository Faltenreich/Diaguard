package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.statistic.daterange.StatisticDateRangeType

sealed interface StatisticIntent {

    data class SetDateRangeType(val dateRangeType: StatisticDateRangeType) : StatisticIntent

    data class SetDateRange(val dateRange: DateRange) : StatisticIntent

    data class SetCategory(val category: MeasurementCategory.Local) : StatisticIntent

    data class SetProperty(val property: MeasurementProperty.Local) : StatisticIntent
}