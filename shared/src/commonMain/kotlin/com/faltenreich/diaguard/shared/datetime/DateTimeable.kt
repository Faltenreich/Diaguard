package com.faltenreich.diaguard.shared.datetime

import com.faltenreich.diaguard.shared.serialization.ObjectInputStream
import com.faltenreich.diaguard.shared.serialization.ObjectOutputStream

interface DateTimeable {

    /**
     * Date component
     */
    val date: Date

    /**
     * Time component
     */
    val time: Time

    /**
     * Milliseconds since 01/01/1970
     */
    val millisSince1970: Long

    /**
     * Date and time in ISO 8601 format
     */
    val isoString: String

    /**
     * Current point in time
     */
    fun now(): DateTimeable

    /**
     * Deserializes date
     */
    fun readObject(inputStream: ObjectInputStream)

    /**
     * Serializes date
     */
    fun writeObject(outputStream: ObjectOutputStream)
}