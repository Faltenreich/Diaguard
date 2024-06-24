package com.faltenreich.diaguard.export

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.csv
import diaguard.shared.generated.resources.pdf
import org.jetbrains.compose.resources.StringResource

enum class ExportType(
    val title: StringResource,
) {

    CSV(Res.string.csv),
    PDF(Res.string.pdf),
}