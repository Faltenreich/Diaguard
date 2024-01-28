package com.faltenreich.diaguard.shared.datetime

class GetTodayUseCase(
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Date {
        return dateTimeFactory.today()
    }
}