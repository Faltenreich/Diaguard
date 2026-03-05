package com.faltenreich.diaguard.persistence.file

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.createdAt
import io.github.vinceglb.filekit.list

class FileRepository(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun getDocuments(): List<File> {
        return FileKit.documentsDir?.list()?.map { file ->
            File(
                absolutePath = file.absolutePath(),
                createdAt = file.createdAt()?.let { instant ->
                    dateTimeFactory.dateTime(
                        millis = instant.toEpochMilliseconds(),
                    )
                },
            )
        } ?: emptyList()
    }
}