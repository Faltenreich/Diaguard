package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class GetExportFilesUseCase(
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<List<ExportFile>> {
        return flowOf(
            listOf(
                ExportFile(
                    dateTime = dateTimeFormatter.formatDateTime(dateTimeFactory.now()),
                    type = ExportType.PDF,
                ),
                ExportFile(
                    dateTime = dateTimeFormatter.formatDateTime(dateTimeFactory.now()),
                    type = ExportType.CSV,
                ),
            ),
        )
    }
}