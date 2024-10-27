package com.faltenreich.diaguard.dashboard.trend

import com.faltenreich.diaguard.dashboard.DashboardState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetTrendUseCase {

    operator fun invoke(): Flow<DashboardState.Trend> {
        // TODO
        return flowOf(DashboardState.Trend(values = emptyMap()))
    }
}