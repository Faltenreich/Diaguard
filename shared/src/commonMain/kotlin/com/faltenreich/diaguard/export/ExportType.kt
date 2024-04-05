package com.faltenreich.diaguard.export

import diaguard.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class ExportType(
    val title: StringResource,
) {

    CSV(Res.string.csv),
    PDF(Res.string.pdf),
}