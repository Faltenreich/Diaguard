package com.faltenreich.diaguard.datetime.format

import com.faltenreich.diaguard.datetime.Date

interface NativeDateTimeFormatter {

    fun formatDate(date: Date): String
}