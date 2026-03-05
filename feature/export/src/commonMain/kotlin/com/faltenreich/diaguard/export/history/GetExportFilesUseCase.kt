package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.persistence.file.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class GetExportFilesUseCase(
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<List<ExportFile>> {
        val files = File.documents()
        return flowOf(
            files.map { file ->
                ExportFile(
                    dateTime = dateTimeFormatter.formatDateTime(dateTimeFactory.now()),
                    type = ExportType.PDF, // TODO
                    file = file,
                )
            }
        )
    }
}