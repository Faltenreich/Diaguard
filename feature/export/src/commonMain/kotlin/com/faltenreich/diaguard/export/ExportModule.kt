package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.data.dataModule
import com.faltenreich.diaguard.export.form.ExportFormViewModel
import com.faltenreich.diaguard.export.form.ExportUseCase
import com.faltenreich.diaguard.export.form.GetExportSettingsUseCase
import com.faltenreich.diaguard.export.form.SetExportCategoryUseCase
import com.faltenreich.diaguard.export.form.SetExportSettingsUseCase
import com.faltenreich.diaguard.export.history.ExportHistoryViewModel
import com.faltenreich.diaguard.export.history.GetExportFilesUseCase
import com.faltenreich.diaguard.export.pdf.CreatePdfPageUseCase
import com.faltenreich.diaguard.export.pdf.ExportPdfUseCase
import com.faltenreich.diaguard.export.pdf.note.PdfNoteListFactory
import com.faltenreich.diaguard.export.pdf.table.PdfTableFactory
import com.faltenreich.diaguard.measurement.measurementModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun exportModule() = module {
    includes(
        dataModule(),
        measurementModule(),
    )

    factoryOf(::GetExportFilesUseCase)
    factoryOf(::GetExportSettingsUseCase)
    factoryOf(::SetExportSettingsUseCase)
    factoryOf(::SetExportCategoryUseCase)
    factoryOf(::ExportUseCase)
    factoryOf(::ExportPdfUseCase)
    factoryOf(::CreatePdfPageUseCase)
    factoryOf(::PdfTableFactory)
    factoryOf(::PdfNoteListFactory)

    viewModelOf(::ExportFormViewModel)
    viewModelOf(::ExportHistoryViewModel)
}