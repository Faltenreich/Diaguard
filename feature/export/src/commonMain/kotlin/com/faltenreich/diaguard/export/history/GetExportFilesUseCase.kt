package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.persistence.file.FileRepository

internal class GetExportFilesUseCase(
    private val fileRepository: FileRepository,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): List<ExportFile> {
        val files = fileRepository.getDocuments()
        return files.mapNotNull { file ->
            ExportType.entries
                .firstOrNull { file.absolutePath.endsWith(it.extension) }
                ?.let { type ->
                    ExportFile(
                        file = file,
                        type = type,
                        dateTime = file.createdAt?.let(dateTimeFormatter::formatDateTime),
                    )
                }

        }
    }
}