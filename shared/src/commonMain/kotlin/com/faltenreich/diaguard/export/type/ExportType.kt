package com.faltenreich.diaguard.export.type

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.export_type_csv
import diaguard.shared.generated.resources.export_type_csv_description
import diaguard.shared.generated.resources.export_type_pdf
import diaguard.shared.generated.resources.export_type_pdf_description
import org.jetbrains.compose.resources.StringResource

enum class ExportType(
    val titleResource: StringResource,
    val descriptionResource: StringResource,
) {

    PDF(
        titleResource = Res.string.export_type_pdf,
        descriptionResource = Res.string.export_type_pdf_description,
    ),
    CSV(
        titleResource = Res.string.export_type_csv,
        descriptionResource = Res.string.export_type_csv_description,
    ),
}