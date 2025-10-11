package com.faltenreich.diaguard.shared.localization

import com.faltenreich.diaguard.datetime.DayOfWeek

interface NativeLocalization {

    fun getStartOfWeek(): DayOfWeek

    fun is24HourFormat(): Boolean
}