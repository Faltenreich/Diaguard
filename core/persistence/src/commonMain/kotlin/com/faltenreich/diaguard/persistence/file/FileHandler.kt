package com.faltenreich.diaguard.persistence.file

interface FileHandler {

    fun open(file: File)

    fun share(file: File)
}