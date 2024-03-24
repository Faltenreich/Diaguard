package com.faltenreich.diaguard.datetime.factory

import com.faltenreich.diaguard.datetime.Date

class GetTodayUseCase(
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Date {
        return dateTimeFactory.today()
    }
}