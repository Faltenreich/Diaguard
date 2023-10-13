package com.faltenreich.diaguard.shared.file

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString

class SystemFileReader(private val path: String) : FileReader {

    override fun invoke(): String {
        return SystemFileSystem.source(Path(path)).buffered().readString()
    }
}