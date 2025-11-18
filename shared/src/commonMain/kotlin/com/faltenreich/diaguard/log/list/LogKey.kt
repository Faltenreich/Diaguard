package com.faltenreich.diaguard.log.list

import com.faltenreich.diaguard.localization.serialization.Serializable
import com.faltenreich.diaguard.datetime.Date

sealed class LogKey(val date: Date) : Serializable {

    class Header(date: Date) : LogKey(date)

    class Item(date: Date, val isFirstOfDay: Boolean) : LogKey(date)
}