package com.faltenreich.diaguard.shared.serialization

expect class ObjectInputStream {

    fun readLong(): Long
}