package com.faltenreich.diaguard.import

interface Import <Output> {

    operator fun invoke(): Output
}