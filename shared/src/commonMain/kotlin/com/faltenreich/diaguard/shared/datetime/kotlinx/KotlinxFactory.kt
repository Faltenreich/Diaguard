package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.DateTimeFactory

class KotlinxFactory : DateTimeFactory<KotlinxDate, KotlinxTime, KotlinxDateTime> {

    override fun now(): KotlinxDateTime {
        return KotlinxDateTime.now()
    }

    override fun fromIsoString(isoString: String): KotlinxDateTime {
        return KotlinxDateTime(isoString)
    }

    override fun fromMillis(millis: Long): KotlinxDateTime {
        return KotlinxDateTime(millis)
    }
}