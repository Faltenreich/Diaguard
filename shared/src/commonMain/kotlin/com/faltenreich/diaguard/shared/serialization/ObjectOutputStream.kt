package com.faltenreich.diaguard.shared.serialization

expect class ObjectOutputStream {

    fun writeBoolean(value: Boolean)

    fun writeByte(value: Int)

    fun writeChar(value: Int)

    fun writeShort(value: Int)

    fun writeInt(value: Int)

    fun writeLong(value: Long)

    fun writeFloat(value: Float)

    fun writeDouble(value: Double)
}