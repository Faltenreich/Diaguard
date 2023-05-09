package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.shared.datetime.DateTime

interface DatabaseEntity {

    /**
     * Unique identifier
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