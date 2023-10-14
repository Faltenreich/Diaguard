package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory

class KotlinxFactory : DateTimeFactory {

    override fun now(): KotlinxDateTime {
        return KotlinxDateTime.now()
    }

    override fun today(): Date {
        return DateTime.now().date
    }

    override fun at(year: Int, monthNumber: Int, dayOfMonth: Int): Date {
        return KotlinxDate(year, monthNumber, dayOfMonth)
    }

    override fun fromIsoString(isoString: String): KotlinxDateTime {
        return KotlinxDateTime(isoString)
    }

    override fun fromMillis(millis: Long): KotlinxDateTime {
        return KotlinxDateTime(millis)
    }
}