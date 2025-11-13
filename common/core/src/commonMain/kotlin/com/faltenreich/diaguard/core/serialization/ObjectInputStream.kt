package com.faltenreich.diaguard.core.serialization

expect class ObjectInputStream {

    fun readBoolean(): Boolean

    fun readByte(): Byte

    fun readChar(): Char

    fun readShort(): Short

    fun readInt(): Int

    fun readLong(): Long

    fun readFloat(): Float

    fun readDouble(): Double
}