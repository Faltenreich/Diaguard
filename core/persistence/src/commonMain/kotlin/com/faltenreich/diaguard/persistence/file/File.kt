package com.faltenreich.diaguard.persistence.file

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.list

data class File(val absolutePath: String) {

    companion object {

        fun documents(): List<File> {
            return FileKit.documentsDir?.list()?.map { file ->
                File(file.absolutePath())
            } ?: emptyList()
        }
    }
}