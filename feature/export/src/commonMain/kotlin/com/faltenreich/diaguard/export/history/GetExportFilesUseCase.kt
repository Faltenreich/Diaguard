package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.persistence.file.documentsDir
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.list
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class GetExportFilesUseCase(
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<List<ExportFile>> {
        val files = FileKit.documentsDir?.list() ?: emptyList()
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