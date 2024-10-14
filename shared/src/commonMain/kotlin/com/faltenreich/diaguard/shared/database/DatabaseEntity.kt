package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.serialization.Serializable

interface DatabaseEntity : Serializable {

    /**
     * Locally unique identifier
     */
    val id: Long

    /**
     * Point in time when this entity has been created
     */
    val createdAt: DateTime

    /**
     * Point in time when this entity has been updated the last time
     */
    val updatedAt: DateTime
}