package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.shared.serialization.Serializable

sealed class LogKey(val date: Date) : Serializable {

    class Header(date: Date) : LogKey(date)

    class Item(date: Date, val isFirstOfDay: Boolean) : LogKey(date)
}