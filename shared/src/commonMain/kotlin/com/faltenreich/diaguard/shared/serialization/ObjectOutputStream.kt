package com.faltenreich.diaguard.shared.serialization

expect class ObjectOutputStream {

    fun writeLong(long: Long)
}