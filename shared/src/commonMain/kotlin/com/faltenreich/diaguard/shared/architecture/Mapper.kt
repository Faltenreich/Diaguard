package com.faltenreich.diaguard.shared.architecture

interface Mapper<INPUT, OUTPUT> {

    fun map(input: INPUT): OUTPUT
}