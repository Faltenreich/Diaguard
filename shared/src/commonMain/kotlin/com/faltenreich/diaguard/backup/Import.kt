package com.faltenreich.diaguard.backup

interface Import<Output> {

    operator fun invoke(): Output
}